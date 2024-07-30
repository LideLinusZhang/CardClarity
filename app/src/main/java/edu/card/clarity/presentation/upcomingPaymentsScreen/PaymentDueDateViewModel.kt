package edu.card.clarity.presentation.upcomingPaymentsScreen

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PaymentDueDateViewModel @Inject constructor(
    cashBackCreditCardRepository: CashBackCreditCardRepository,
    pointBackCreditCardRepository: PointBackCreditCardRepository
) : ViewModel() {
    private val _creditCards = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }

    val uiState: StateFlow<List<PaymentDueDateUiState>> = _creditCards
        .map { cardList -> cardList.map { it.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = listOf()
        )

    fun getNextPayment(cards: List<PaymentDueDateUiState>): Pair<String, LocalDate>? {
        if (cards.isEmpty()) {
            return null
        }

        val today = LocalDate.now()
        val todayDayOfMonth = today.dayOfMonth
        val todayMonth = today.monthValue

        // sort payments by due date
        val sortedPayments = cards.sortedBy { it.dueDate.toLocalDate() }

        // find the first payment that is due later this month
        val nextPaymentThisMonth = sortedPayments.firstOrNull {
            it.dueDate.get(Calendar.DAY_OF_MONTH) > todayDayOfMonth &&
                    it.dueDate.get(Calendar.MONTH) + 1 == todayMonth
        }

        // if no payment is due later this month, take the earliest due date next month
        val nextPayment = nextPaymentThisMonth ?: sortedPayments.firstOrNull()

        return nextPayment?.let {
            // adjust for indexing
            val dueMonth = it.dueDate.get(Calendar.MONTH) + 1
            // wrap around to next year
            val dueYear = if (dueMonth < todayMonth) {
                it.dueDate.get(Calendar.YEAR) + 1
            } else {
                it.dueDate.get(Calendar.YEAR)
            }

            // add one month if we are wrapping to the earliest due date next month
            val adjustedMonth = if (nextPaymentThisMonth == null) dueMonth + 1 else dueMonth

            val dueDate =
                LocalDate.of(dueYear, adjustedMonth, it.dueDate.get(Calendar.DAY_OF_MONTH))
            Pair(it.cardName, dueDate)
        }
    }

    private fun Calendar.toLocalDate(): LocalDate {
        val instant = this.toInstant()
        val zoneId = ZoneId.systemDefault()
        return instant.atZone(zoneId).toLocalDate()
    }

    companion object {
        private fun CreditCardInfo.toUiState() = PaymentDueDateUiState(
            cardName = name,
            dueDate = paymentDueDate,
            backgroundColor = when (cardNetworkType) {
                CardNetworkType.Visa -> Color(0xFFC8E6C9)  // light green
                CardNetworkType.MasterCard -> Color(0xFFFFCDD2)  // light red
                CardNetworkType.AMEX -> Color(0xFFBBDEFB)  // light blue
            }
        )

        private fun Calendar.toInstant(): Instant {
            return this.time.toInstant()
        }
    }
}

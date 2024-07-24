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
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PaymentDueDateViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository
) : ViewModel() {

    private val _filteredCreditCards = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }

    val uiState: StateFlow<List<PaymentDueDateUiState>> = _filteredCreditCards
        .map { cardList -> cardList.map { it.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = listOf()
        )

    fun getNextPayment(): PaymentDueDateUiState? {
        val sortedPayments = uiState.value.sortedBy { parseDate(it.dueDate) }
        return sortedPayments.firstOrNull { parseDate(it.dueDate).isAfter(LocalDate.now()) }
    }

    private fun parseDate(calendar: Calendar): LocalDate {
        val instant = calendar.toInstant()
        val zoneId = ZoneId.systemDefault()
        return instant.atZone(zoneId).toLocalDate()
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        private fun CreditCardInfo.toUiState() = PaymentDueDateUiState(
            cardName = name,
            dueDate = paymentDueDate,
            backgroundColor = when (cardNetworkType) {
                CardNetworkType.Visa -> Color.Green
                CardNetworkType.MasterCard -> Color.Red
                CardNetworkType.AMEX -> Color.Blue
            }
        )

        private fun Calendar.toInstant(): Instant {
            return this.time.toInstant()
        }
    }
}

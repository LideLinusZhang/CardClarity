package edu.card.clarity.presentation.addCardScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.displayStrings
import edu.card.clarity.repositories.PointSystemRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardInformationFormViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val pointSystemRepository: PointSystemRepository,
) : ViewModel() {
    val cardNetworkTypeStrings = CardNetworkType.displayStrings
    val rewardTypeOptionStrings = RewardType.displayStrings

    private val _uiState = MutableStateFlow(
        CardInformationFormUiState(
            selectedCardNetworkType = cardNetworkTypeStrings.first(),
            selectedRewardType = rewardTypeOptionStrings.first()
        )
    )
    val uiState: StateFlow<CardInformationFormUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private var selectedCardNetworkType: CardNetworkType = CardNetworkType.entries.first()
    private var selectedRewardType: RewardType = RewardType.entries.first()
    private val mostRecentStatementDate = Calendar.getInstance()
    private val mostRecentPaymentDueDate = Calendar.getInstance()

    fun updateCardName(name: String) {
        _uiState.update {
            it.copy(cardName = name)
        }
    }

    fun updateSelectedCardNetworkType(index: Int) {
        selectedCardNetworkType = CardNetworkType.entries[index]

        _uiState.update {
            it.copy(selectedCardNetworkType = cardNetworkTypeStrings[index])
        }
    }

    fun updateSelectedRewardType(index: Int) {
        selectedRewardType = RewardType.entries[index]

        _uiState.update {
            it.copy(selectedRewardType = rewardTypeOptionStrings[index])
        }
    }

    fun updateMostRecentStatementDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentStatementDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentStatementDate = dateFormatter.format(mostRecentStatementDate.time)
            )
        }
    }

    fun updateReminderEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                isReminderEnabled = isEnabled
            )
        }
    }

    fun updateMostRecentPaymentDueDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentPaymentDueDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentPaymentDueDate = dateFormatter.format(mostRecentPaymentDueDate.time)
            )
        }
    }

    fun updatePointSystemName(name: String) {
        _uiState.update {
            it.copy(pointSystemName = name)
        }
    }

    fun updatePointToCashConversionRate(rate: String) {
        _uiState.update {
            it.copy(pointToCashConversionRate = rate)
        }
    }

    fun addCreditCard() = viewModelScope.launch {
        val creditCardInfo = CreditCardInfo(
            name = uiState.value.cardName,
            rewardType = selectedRewardType,
            cardNetworkType = selectedCardNetworkType,
            statementDate = mostRecentStatementDate,
            paymentDueDate = mostRecentPaymentDueDate,
            isReminderEnabled = uiState.value.isReminderEnabled,
        )

        if (selectedRewardType == RewardType.CashBack) {
            cashBackCreditCardRepository.createCreditCard(creditCardInfo)
        } else {
            val pointSystem = PointSystem(
                name = uiState.value.pointSystemName,
                pointToCashConversionRate = uiState.value.pointToCashConversionRate.toFloat()
            )
            val pointSystemId = pointSystemRepository.addPointSystem(pointSystem)
            pointBackCreditCardRepository.createCreditCard(creditCardInfo, pointSystemId)
        }

        _uiState.update {
            CardInformationFormUiState(
                selectedCardNetworkType = cardNetworkTypeStrings.first(),
                selectedRewardType = rewardTypeOptionStrings.first()
            )
        }
    }
}
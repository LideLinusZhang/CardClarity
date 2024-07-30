package edu.card.clarity.presentation.addCardScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.ICreditCard
import edu.card.clarity.domain.creditCard.PointBackCreditCard
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayString
import edu.card.clarity.repositories.PointSystemRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateSelectionFormViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val pointSystemRepository: PointSystemRepository,
) : ViewModel() {
    private val cashBackTemplates = cashBackCreditCardRepository
        .getAllPredefinedCreditCardsStream()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private val pointBackTemplates = pointBackCreditCardRepository
        .getAllPredefinedCreditCardsStream()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private val allTemplates = combine(cashBackTemplates, pointBackTemplates) { cashBacks, pointBacks ->
        cashBacks + pointBacks
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = emptyList()
    )

    val templateOptionStrings: StateFlow<List<String>> = allTemplates
        .map { it.map { card -> card.info.name } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private val mostRecentStatementDate = Calendar.getInstance()
    private val mostRecentPaymentDueDate = Calendar.getInstance()

    private var selectedTemplate: ICreditCard? = null

    private val _uiState = MutableStateFlow(TemplateSelectionFormUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTemplateSelection(selectedIndex: Int) {
        selectedTemplate = allTemplates.value[selectedIndex]
        Log.d("selected template", selectedTemplate?.info.toString())
        Log.d("selected template", selectedTemplate?.purchaseRewards.toString())
        selectedTemplate?.info?.let {
            _uiState.value = _uiState.value.copy(
                selectedTemplateName = it.name,
                showCardInfo = true,
                cardName = it.name,
                rewardType = it.rewardType.displayString,
                cardNetworkType = it.cardNetworkType.name
            )
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

    fun updateMostRecentPaymentDueDate(year: Int, month: Int, dayOfMonth: Int) {
        mostRecentPaymentDueDate.set(year, month, dayOfMonth)

        _uiState.update {
            it.copy(
                mostRecentPaymentDueDate = dateFormatter.format(mostRecentPaymentDueDate.time)
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

    fun createCreditCard() {
        val selectedCard = selectedTemplate ?: return

        viewModelScope.launch {
            val cardInfo = CreditCardInfo(
                name = _uiState.value.cardName,
                rewardType = when (selectedCard) {
                    is CashBackCreditCard -> RewardType.CashBack
                    is PointBackCreditCard -> RewardType.PointBack
                    else -> return@launch
                },
                cardNetworkType = selectedCard.info.cardNetworkType,
                statementDate = mostRecentStatementDate,
                paymentDueDate = mostRecentPaymentDueDate,
                isReminderEnabled = _uiState.value.isReminderEnabled
            )

            when (selectedCard) {
                is CashBackCreditCard -> {
                    val newCardId = cashBackCreditCardRepository.createCreditCard(cardInfo)
                    selectedCard.purchaseRewards.forEach { reward ->
                        cashBackCreditCardRepository.addPurchaseReward(
                            creditCardId = newCardId,
                            purchaseTypes = listOf(reward.applicablePurchaseType),
                            percentage = reward.rewardFactor
                        )
                    }
                }
                is PointBackCreditCard -> {
                    val pointSystem = PointSystem(
                        name = selectedCard.pointSystem.name,
                        pointToCashConversionRate = selectedCard.pointSystem.pointToCashConversionRate
                    )
                    val pointSystemId = pointSystemRepository.addPointSystem(pointSystem)
                    val newCardId = pointBackCreditCardRepository.createCreditCard(cardInfo, pointSystemId)
                    selectedCard.purchaseRewards.forEach { reward ->
                        pointBackCreditCardRepository.addPurchaseReward(
                            creditCardId = newCardId,
                            purchaseTypes = listOf(reward.applicablePurchaseType),
                            multiplier = reward.rewardFactor
                        )
                    }
                }
            }
        }
    }
}

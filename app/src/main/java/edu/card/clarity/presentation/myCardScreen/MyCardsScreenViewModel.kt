package edu.card.clarity.presentation.myCardScreen

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyCardsScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedFilter = savedStateHandle.getStateFlow(
        MY_CARDS_SCREEN_SAVED_FILTER_KEY,
        CreditCardFilter()
    )

    private val _filteredCreditCards = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream(),
        _savedFilter
    ) { cashBack, pointBack, filter ->
        filter.filter(cashBack + pointBack)
    }

    val uiState: StateFlow<List<CreditCardItemUiState>> = _filteredCreditCards
        .map { cardList -> cardList.map { it.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = listOf()
        )

    fun addFilter(rewardType: RewardType) {
        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let {
            it.copy(filteredRewardTypes = it.filteredRewardTypes.plus(rewardType))
        }
    }

    fun addFilter(cardNetworkType: CardNetworkType) {
        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let {
            it.copy(filteredCardNetworkTypes = it.filteredCardNetworkTypes.plus(cardNetworkType))
        }
    }

    fun removeFilter(rewardType: RewardType) {
        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let {
            it.copy(filteredRewardTypes = it.filteredRewardTypes.minus(rewardType))
        }
    }

    fun removeFilter(cardNetworkType: CardNetworkType) {
        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let {
            it.copy(filteredCardNetworkTypes = it.filteredCardNetworkTypes.minus(cardNetworkType))
        }
    }

    companion object {
        private const val MY_CARDS_SCREEN_SAVED_FILTER_KEY = "MY_CARDS_SCREEN_SAVED_FILTER"

        private val dateFormatter = SimpleDateFormat.getDateInstance()

        private fun CreditCardInfo.toUiState() = CreditCardItemUiState(
            name,
            dateFormatter.format(paymentDueDate.timeInMillis),
            when (cardNetworkType) {
                CardNetworkType.Visa -> Color(0xFFB7FF9E)
                CardNetworkType.MasterCard -> Color(0xFFFF9EB8)
                CardNetworkType.AMEX -> Color(0xFFAED8FF)
            }
        )
    }
}
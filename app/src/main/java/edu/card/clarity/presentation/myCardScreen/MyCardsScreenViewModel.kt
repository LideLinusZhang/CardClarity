package edu.card.clarity.presentation.myCardScreen

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.data.alarmItem.AndroidAlarmScheduler
import edu.card.clarity.data.alarmItem.AlarmItemDao
import edu.card.clarity.data.alarmItem.toSchedulerAlarmItem
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayStrings
import edu.card.clarity.presentation.utils.ordinals
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyCardsScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val creditCardDao: CreditCardDao,
    private val alarmItemDao: AlarmItemDao,
    private val scheduler: AndroidAlarmScheduler,
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

    val rewardTypeFilterOptionStrings = RewardType.displayStrings
    val cardNetworkTypeFilterOptionStrings = CardNetworkType.displayStrings

    val rewardTypeFilterInitiallySelectedOptionIndices = RewardType.ordinals
    val cardNetworkTypeFilterInitiallySelectedOptionIndices = CardNetworkType.ordinals

    val uiState: StateFlow<List<CreditCardItemUiState>> = _filteredCreditCards
        .map { cardList -> cardList.map { it.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = listOf()
        )

    fun updateRewardTypeFilter(rewardTypeOrdinal: Int, isAddFilter: Boolean) {
        require(rewardTypeOrdinal in RewardType.ordinals)

        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let { filter ->
            filter.copy(filteredRewardTypes = filter.filteredRewardTypes.let {
                val rewardTypeToUpdate = RewardType.entries[rewardTypeOrdinal]

                if (isAddFilter) it.plus(rewardTypeToUpdate)
                else it.minus(rewardTypeToUpdate)
            })
        }
    }

    fun updateCardNetworkFilter(cardNetworkTypeOrdinal: Int, isAddFilter: Boolean) {
        require(cardNetworkTypeOrdinal in CardNetworkType.ordinals)

        savedStateHandle[MY_CARDS_SCREEN_SAVED_FILTER_KEY] = _savedFilter.value.let { filter ->
            filter.copy(filteredCardNetworkTypes = filter.filteredCardNetworkTypes.let {
                val cardNetworkTypeToUpdate = CardNetworkType.entries[cardNetworkTypeOrdinal]

                if (isAddFilter) it.plus(cardNetworkTypeToUpdate)
                else it.minus(cardNetworkTypeToUpdate)
            })
        }
    }

    fun deleteCreditCard(id: UUID) {
        viewModelScope.launch {
            val alarmItem = creditCardDao.getAlarmItemByCreditCardId(id)

            alarmItem?.let {
                scheduler.cancel(it.toSchedulerAlarmItem())
                alarmItemDao.deleteById(it.id)
            }

            cashBackCreditCardRepository.deleteCreditCard(id)
            pointBackCreditCardRepository.deleteCreditCard(id)
        }
    }

    companion object {
        private const val MY_CARDS_SCREEN_SAVED_FILTER_KEY: String = "MY_CARDS_SCREEN_SAVED_FILTER"

        private val dateFormatter = SimpleDateFormat.getDateInstance()

        private fun CreditCardInfo.toUiState() = CreditCardItemUiState(
            id!!,
            name,
            rewardType.ordinal,
            dateFormatter.format(paymentDueDate.timeInMillis),
            isReminderEnabled,
            when (cardNetworkType) {
                CardNetworkType.Visa -> Color(0xFFB7FF9E)
                CardNetworkType.MasterCard -> Color(0xFFFF9EB8)
                CardNetworkType.AMEX -> Color(0xFFAED8FF)
            }
        )
    }
}
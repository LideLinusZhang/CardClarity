package edu.card.clarity.presentation.myBenefitsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.ArgumentNames
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyBenefitsScreenViewModel @Inject constructor(
    cashBackCreditCardRepository: CashBackCreditCardRepository,
    pointBackCreditCardRepository: PointBackCreditCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val cardIdString: String = savedStateHandle[ArgumentNames.CREDIT_CARD_ID]!!
    val cardRewardTypeOrdinal: Int = savedStateHandle[ArgumentNames.CREDIT_CARD_REWARD_TYPE]!!

    private val cardId: UUID = UUID.fromString(cardIdString)
    private val cardRewardType = RewardType.entries[cardRewardTypeOrdinal]

    private val _selectedFilter = MutableStateFlow("ALL")

    val uiState: StateFlow<List<RewardInfo>> = combine(
        when (cardRewardType) {
            RewardType.CashBack -> cashBackCreditCardRepository
                .getCreditCardStream(cardId)
                .map { it.purchaseRewards.map { reward -> reward.toUiState(RewardType.CashBack) } }
            RewardType.PointBack -> pointBackCreditCardRepository
                .getCreditCardStream(cardId)
                .map { it.purchaseRewards.map { reward -> reward.toUiState(RewardType.PointBack) } }
        },
        _selectedFilter
    ) { rewards, filter ->
        if (filter == "ALL") rewards else rewards.filter { it.purchaseType == filter }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = emptyList()
    )

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    private fun PurchaseReward.toUiState(rewardType: RewardType) = RewardInfo(
        purchaseType = this.applicablePurchaseType.name,
        rewardDescription = when (rewardType) {
            RewardType.CashBack -> "${(this.rewardFactor * 100).toInt()}% Cashback"
            RewardType.PointBack -> "${this.rewardFactor}x Points"
        }
    )

    data class RewardInfo(
        val purchaseType: String,
        val rewardDescription: String
    )
}

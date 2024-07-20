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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyBenefitsScreenViewModel @Inject constructor(
    cashBackCreditCardRepository: CashBackCreditCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val cardIdString: String = savedStateHandle[ArgumentNames.CREDIT_CARD_ID]!!
    val cardRewardTypeOrdinal: Int = savedStateHandle[ArgumentNames.CREDIT_CARD_REWARD_TYPE]!!

    private val cardId: UUID = UUID.fromString(cardIdString)

    val uiState: StateFlow<List<RewardInfo>> = cashBackCreditCardRepository
        .getCreditCardStream(cardId)
        .map { it.purchaseRewards.map { reward -> reward.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private companion object {
        private fun PurchaseReward.toUiState() = RewardInfo(
            purchaseType = this.applicablePurchaseType.name,
            rewardDescription = "${(this.rewardFactor * 100).toInt()}% Cashback"
        )
    }
}

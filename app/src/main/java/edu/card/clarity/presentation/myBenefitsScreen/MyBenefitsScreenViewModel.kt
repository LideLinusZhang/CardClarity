package edu.card.clarity.presentation.myBenefitsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyBenefitsScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
) : ViewModel() {

    private val _benefitItems = MutableStateFlow<List<BenefitInfo>>(emptyList())
    val benefitItems: StateFlow<List<BenefitInfo>> = _benefitItems

    fun loadBenefits(cardId: UUID?) {
        if (cardId == null) {
            _benefitItems.value = emptyList()
            Log.d("MyBenefitsScreenVM", "No cardId provided, setting empty benefit items.")
            return
        }

        viewModelScope.launch {
            val card = cashBackCreditCardRepository.getCreditCard(cardId)
            if (card != null) {
                Log.d("MyBenefitsScreenVM", "Card fetched purchaseRewards: ${card.purchaseRewards}")
            }
            card?.let {
                val benefits = it.purchaseRewards.map { reward ->
                    BenefitInfo(
                        purchaseType = reward.applicablePurchaseType.name,
                        benefit = "${(reward.rewardFactor * 100).toInt()}% cashback"
                    )
                }
                _benefitItems.value = benefits
                Log.d("MyBenefitsScreenVM", "Benefits loaded: $benefits")
            } ?: run {
                Log.d("MyBenefitsScreenVM", "No card found for cardId: $cardId")
            }
        }
    }

    suspend fun getCardRewardType(cardId: UUID?): String {
        if (cardId == null) return "Unknown"
        val card = cashBackCreditCardRepository.getCreditCard(cardId)
        return card?.info?.rewardType?.name ?: "Unknown"
    }

    fun addBenefit(cardId: UUID?, purchaseType: PurchaseType, rewardValue: Float) {
        if (cardId == null) return

        viewModelScope.launch {
            val cardRewardType = getCardRewardType(cardId)
            if (cardRewardType == "CashBack") {
                val card = cashBackCreditCardRepository.getCreditCard(cardId)
                card?.let {
                    val existingRewards = it.purchaseRewards.toMutableList()
                    existingRewards.add(PurchaseReward(purchaseType, rewardValue))
                    cashBackCreditCardRepository.addPurchaseReward(cardId, listOf(purchaseType), rewardValue)
                    loadBenefits(cardId)
                }
            }
        }
    }
}

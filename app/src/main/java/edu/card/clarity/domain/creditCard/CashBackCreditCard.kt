package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.PurchaseType

data class CashBackCreditCard(
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>
) : ICreditCard {
    override fun getReturnAmountInCash(purchaseTotal: Float, purchaseType: PurchaseType): Float {
        val purchaseReward = purchaseRewards.let {
            it.firstOrNull { reward ->
                reward.applicablePurchaseType == purchaseType
            } ?: it.firstOrNull { reward ->
                reward.applicablePurchaseType == PurchaseType.Others
            }
        } ?: return 0.0f

        return purchaseReward.getReturnAmount(purchaseTotal)
    }
}
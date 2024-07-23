package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.PurchaseType

data class PointBackCreditCard(
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>,
    val pointSystem: PointSystem
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        return getReturnAmountInPoint(purchase) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchase: Purchase): Int {
        val purchaseReturn = purchaseRewards.let {
            it.firstOrNull { reward ->
                reward.applicablePurchaseType == purchase.type
            } ?: it.firstOrNull { reward ->
                reward.applicablePurchaseType == PurchaseType.Others
            }
        } ?: return 0

        return purchaseReturn.getReturnAmount(purchase.total).toInt()
    }
}
package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.PurchaseType

data class PointBackCreditCard(
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>,
    val pointSystem: PointSystem
) : ICreditCard {
    override fun getReturnAmountInCash(purchaseTotal: Float, purchaseType: PurchaseType): Float {
        return getReturnAmountInPoint(
            purchaseTotal,
            purchaseType
        ) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchaseTotal: Float, purchaseType: PurchaseType): Int {
        val purchaseReturn = purchaseRewards.let {
            it.firstOrNull { reward ->
                reward.applicablePurchaseType == purchaseType
            } ?: it.firstOrNull { reward ->
                reward.applicablePurchaseType == PurchaseType.Others
            }
        } ?: return 0

        return purchaseReturn.getReturnAmount(purchaseTotal).toInt()
    }
}
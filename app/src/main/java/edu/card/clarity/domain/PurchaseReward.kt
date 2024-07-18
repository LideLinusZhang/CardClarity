package edu.card.clarity.domain

import edu.card.clarity.enums.PurchaseType

data class PurchaseReward(
    val applicablePurchaseType: PurchaseType,
    val rewardFactor: Float
) {
    fun getReturnAmount(purchaseTotal: Float): Float {
        return purchaseTotal * rewardFactor
    }
}

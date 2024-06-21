package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase

class PercentagePurchaseReturn(
    val percentage: Float,
    override val applicablePurchaseTypes: List<PurchaseType>
): IPurchaseReturn {
    init {
        require(0 <= percentage && percentage < 1.0)
    }

    override fun getReturnAmount(purchase: Purchase): Float {
        require(applicablePurchaseTypes.contains(purchase.type))

        return purchase.amount * percentage
    }
}
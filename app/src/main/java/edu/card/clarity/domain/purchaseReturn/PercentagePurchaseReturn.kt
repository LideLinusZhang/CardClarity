package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseType

class PercentagePurchaseReturn(
    override val applicablePurchaseType: PurchaseType,
    val percentage: Float
): IPurchaseReturn {
    init {
        require(0 <= percentage && percentage < 1.0)
    }

    override fun getReturnAmount(purchase: Purchase): Float {
        require(applicablePurchaseType == purchase.type)

        return purchase.amount * percentage
    }
}
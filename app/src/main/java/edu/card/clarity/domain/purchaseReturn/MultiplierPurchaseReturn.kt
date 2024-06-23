package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseType

class MultiplierPurchaseReturn(
    val multiplier: Float,
    override val applicablePurchaseType: PurchaseType
) : IPurchaseReturn {
    init {
        require(multiplier >= 1.0)
    }

    override fun getReturnAmount(purchase: Purchase): Float {
        require(applicablePurchaseType == purchase.type)

        return purchase.amount * multiplier
    }
}
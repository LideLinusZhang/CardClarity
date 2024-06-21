package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase

class MultiplierPurchaseReturn(
    val multiplier: Float,
    override val applicablePurchaseTypes: List<PurchaseType>
) : IPurchaseReturn {
    init {
        require(multiplier >= 1.0)
    }

    override fun getReturnAmount(purchase: Purchase): Float {
        require(applicablePurchaseTypes.contains(purchase.type))

        return purchase.amount * multiplier
    }
}
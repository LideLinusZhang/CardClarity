package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase

interface IPurchaseReturn {
    val applicablePurchaseTypes: List<PurchaseType>

    fun getReturnAmount(purchase: Purchase): Float
}

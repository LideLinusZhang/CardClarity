package edu.card.clarity.domain.purchaseReturn

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseType

interface IPurchaseReturn {
    val applicablePurchaseType: PurchaseType

    fun getReturnAmount(purchase: Purchase): Float
}

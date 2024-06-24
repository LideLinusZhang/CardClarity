package edu.card.clarity.domain

data class PurchaseReturn(
    val applicablePurchaseType: PurchaseType,
    val returnFactor: Float
) {

    fun getReturnAmount(purchase: Purchase): Float {
        return purchase.amount * returnFactor
    }
}

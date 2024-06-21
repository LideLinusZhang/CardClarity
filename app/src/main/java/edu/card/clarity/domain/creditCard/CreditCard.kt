package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.IPurchaseReturn
import java.util.Date

abstract class CreditCard<TPurchaseReturn : IPurchaseReturn>(
    val name: String,
    val statementDate: Date,
    val paymentDueDate: Date,
    val purchaseReturns: MutableList<TPurchaseReturn> = mutableListOf()
) {
    fun addPurchaseReturn(purchaseReturn: TPurchaseReturn) {
        purchaseReturns.add(purchaseReturn)
    }

    abstract fun getReturnAmountInCash(purchase: Purchase): Float
}
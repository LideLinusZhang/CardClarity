package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.IPurchaseReturn
import java.util.Date
import java.util.UUID

abstract class CreditCard<TPurchaseReturn: IPurchaseReturn>(
    val id: UUID,
    val name: String,
    val statementDate: Date,
    val paymentDueDate: Date,
    val purchaseReturns: MutableList<TPurchaseReturn>
) {
    fun addPurchaseReturn(purchaseReturn: TPurchaseReturn) {
        purchaseReturns.add(purchaseReturn)
    }

    abstract fun getReturnAmountInCash(purchase: Purchase): Float
}
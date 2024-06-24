package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReturn
import java.util.UUID

interface ICreditCard {
    val id: UUID
    val info: CreditCardInfo
    val purchaseReturns: List<PurchaseReturn>

    fun getReturnAmountInCash(purchase: Purchase): Float
}
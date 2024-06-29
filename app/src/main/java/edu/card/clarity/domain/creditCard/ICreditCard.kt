package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import java.util.UUID

interface ICreditCard {
    val id: UUID
    val info: CreditCardInfo
    val purchaseRewards: List<PurchaseReward>

    fun getReturnAmountInCash(purchase: Purchase): Float
}
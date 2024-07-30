package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.PurchaseType

interface ICreditCard {
    val info: CreditCardInfo
    val purchaseRewards: List<PurchaseReward>

    fun getReturnAmountInCash(purchaseTotal: Float, purchaseType: PurchaseType): Float
}
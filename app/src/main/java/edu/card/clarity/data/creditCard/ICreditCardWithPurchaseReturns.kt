package edu.card.clarity.data.creditCard

import edu.card.clarity.data.purchaseReturn.IPurchaseReturnEntity

interface ICreditCardWithPurchaseReturns {
    val creditCard: CreditCardEntity
    val purchaseReturns: List<IPurchaseReturnEntity>
}
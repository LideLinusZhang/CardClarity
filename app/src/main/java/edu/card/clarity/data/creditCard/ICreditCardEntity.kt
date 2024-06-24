package edu.card.clarity.data.creditCard

import edu.card.clarity.data.purchaseReturn.IPurchaseReturnEntity

interface ICreditCardEntity {
    val creditCardInfo: ICreditCardInfoEntity
    val purchaseReturns: List<IPurchaseReturnEntity>
}
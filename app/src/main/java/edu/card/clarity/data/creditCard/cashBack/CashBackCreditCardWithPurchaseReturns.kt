package edu.card.clarity.data.creditCard.cashBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCardWithPurchaseReturns
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity

data class CashBackCreditCardWithPurchaseReturns(
    @Embedded override val creditCard: CashBackCreditCardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseReturns: List<MultiplierPurchaseReturnEntity>
) : ICreditCardWithPurchaseReturns

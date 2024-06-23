package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCardWithPurchaseReturns
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity

data class PointBackCreditCardWithPurchaseReturns(
    @Embedded override val creditCard: PointBackCreditCardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseReturns: List<MultiplierPurchaseReturnEntity>
) : ICreditCardWithPurchaseReturns

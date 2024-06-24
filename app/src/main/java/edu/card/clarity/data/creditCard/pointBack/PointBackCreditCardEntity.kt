package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCardEntity
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity

data class PointBackCreditCardEntity(
    @Embedded override val creditCardInfo: PointBackCreditCardInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseReturns: List<MultiplierPurchaseReturnEntity>,
    @Relation(
        parentColumn = "pointSystemId",
        entityColumn = "id"
    )
    val pointSystem: PointSystemEntity
) : ICreditCardEntity

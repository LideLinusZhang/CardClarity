package edu.card.clarity.data.pointSystem

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.CreditCardInfoEntity

data class PointSystemWithCreditCards(
    @Embedded val pointSystem: PointSystemEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pointSystemId"
    )
    val creditCards: List<CreditCardInfoEntity>
)

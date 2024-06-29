package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.pointSystem.PointSystemEntity

data class CreditCardPointSystemAssociation(
    @Embedded val idPair: CreditCardIdPointSystemIdPair,
    @Relation(
        entity = CreditCardIdPointSystemIdPair::class,
        parentColumn = "pointSystemId",
        entityColumn = "id"
    )
    val pointSystem: PointSystemEntity
)

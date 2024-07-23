package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.pointSystem.PointSystem

data class CreditCardPointSystemAssociation(
    @Embedded val idPair: CreditCardIdPointSystemIdPair,
    @Relation(
        parentColumn = "pointSystemId",
        entityColumn = "id"
    )
    val pointSystem: PointSystem
)

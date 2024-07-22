package edu.card.clarity.data.pointSystem

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPair
import java.util.UUID

data class PointSystemWithCreditCardIds(
    @Embedded val pointSystem: PointSystem,
    @Relation(
        entity = CreditCardIdPointSystemIdPair::class,
        parentColumn = "id",
        entityColumn = "pointSystemId",
        projection = ["creditCardId"]
    )
    val creditCardIds: List<UUID>
)

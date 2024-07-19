package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import edu.card.clarity.data.creditCard.CreditCardInfo
import edu.card.clarity.data.pointSystem.PointSystem
import java.util.UUID

@Entity(
    tableName = "pointBackCardPointSystemAssociation",
    primaryKeys = ["creditCardId", "pointSystemId"],
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfo::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PointSystem::class,
            parentColumns = ["id"],
            childColumns = ["pointSystemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("creditCardId", unique = true),
        Index("pointSystemId", unique = true)
    ]
)
data class CreditCardIdPointSystemIdPair(
    val creditCardId: UUID,
    val pointSystemId: UUID,
)

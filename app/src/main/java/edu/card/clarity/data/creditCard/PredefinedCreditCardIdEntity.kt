package edu.card.clarity.data.creditCard

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * A database entity data class that stores the ID of a predefined credit card.
 */
@Entity(
    tableName = "predefinedCreditCardId",
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"]
        )
    ],
    indices = [
        Index("creditCardId", unique = true)
    ]
)
data class PredefinedCreditCardIdEntity(
    @PrimaryKey val creditCardId: UUID
)

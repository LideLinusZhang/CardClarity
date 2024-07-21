package edu.card.clarity.data.creditCard.predefined

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.card.clarity.data.creditCard.CreditCardInfo
import java.util.UUID

/**
 * A database entity data class that stores the ID of a predefined credit card.
 */
@Entity(
    tableName = "predefinedCreditCardId",
    foreignKeys = [
        ForeignKey(
            entity = CreditCardInfo::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"]
        )
    ],
    indices = [
        Index("creditCardId", unique = true)
    ]
)
data class PredefinedCreditCardId(
    @PrimaryKey val creditCardId: UUID
)

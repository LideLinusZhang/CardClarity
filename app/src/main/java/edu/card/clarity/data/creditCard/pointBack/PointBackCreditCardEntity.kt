package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Entity
import androidx.room.ForeignKey
import edu.card.clarity.data.creditCard.CreditCardEntity
import edu.card.clarity.data.pointSystem.PointSystemEntity
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "pointBackCreditCard",
    foreignKeys = [
        ForeignKey(
            entity = PointSystemEntity::class,
            parentColumns = ["id"],
            childColumns = ["pointSystemId"]
        )
    ]
)
data class PointBackCreditCardEntity(
    override val name: String,
    override val statementDate: Date,
    override val paymentDueDate: Date,
    val pointSystemId: UUID
) : CreditCardEntity()

package edu.card.clarity.data.creditCard.pointBack

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.CalendarConverter
import edu.card.clarity.data.creditCard.ICreditCardInfoEntity
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
    ],
    indices = [Index("pointSystemId")]
)
@TypeConverters(CalendarConverter::class)
data class PointBackCreditCardInfoEntity(
    @PrimaryKey override val id: UUID,
    override val name: String,
    override val statementDate: Calendar,
    override val paymentDueDate: Calendar,
    val pointSystemId: UUID,
) : ICreditCardInfoEntity

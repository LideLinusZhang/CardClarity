package edu.card.clarity.data.alarmItem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.LocalDateTimeConverter
import edu.card.clarity.data.creditCard.CreditCardInfo
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    tableName = "alarmItems",
    foreignKeys = [ForeignKey(
        entity = CreditCardInfo::class,
        parentColumns = ["id"],
        childColumns = ["creditCardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(LocalDateTimeConverter::class)
data class AlarmItem(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val time: LocalDateTime,
    val message: String,
    @ColumnInfo(index = true) val creditCardId: UUID
)
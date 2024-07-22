package edu.card.clarity.data.creditCard

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.CalendarConverter
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import java.util.UUID

/**
 * A database entity data class that mirrors [edu.card.clarity.domain.creditCard.CreditCardInfo].
 */
@Entity(
    tableName = "creditCardInfo",
    indices = [
        Index("rewardType"),
        Index("cardNetworkType")
    ]
)
@TypeConverters(CalendarConverter::class)
data class CreditCardInfoEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val rewardType: RewardType,
    val cardNetworkType: CardNetworkType,
    val statementDate: Calendar,
    val paymentDueDate: Calendar,
    @ColumnInfo(defaultValue = "0")
    val isReminderEnabled: Boolean,
)
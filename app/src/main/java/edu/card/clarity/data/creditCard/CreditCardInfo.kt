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
data class CreditCardInfo(
    @PrimaryKey override val id: UUID,
    override val name: String,
    override val rewardType: RewardType,
    override val cardNetworkType: CardNetworkType,
    override val statementDate: Calendar,
    override val paymentDueDate: Calendar,
    @ColumnInfo(defaultValue = "0")
    override val isReminderEnabled: Boolean,
) : ICreditCardInfo
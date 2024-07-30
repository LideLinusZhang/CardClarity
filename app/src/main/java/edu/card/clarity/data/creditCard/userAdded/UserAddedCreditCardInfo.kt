package edu.card.clarity.data.creditCard.userAdded

import android.icu.util.Calendar
import androidx.room.DatabaseView
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.CalendarConverter
import edu.card.clarity.data.creditCard.ICreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import java.util.UUID

@DatabaseView(
    "SELECT creditCardInfo.* FROM creditCardInfo " +
            "LEFT JOIN predefinedCreditCardId " +
            "ON creditCardInfo.id = predefinedCreditCardId.creditCardId " +
            "WHERE predefinedCreditCardId.creditCardId IS NULL",
    viewName = "userAddedCreditCard"
)
@TypeConverters(CalendarConverter::class)
data class UserAddedCreditCardInfo(
    override val id: UUID,
    override val name: String,
    override val rewardType: RewardType,
    override val cardNetworkType: CardNetworkType,
    override val statementDate: Calendar,
    override val paymentDueDate: Calendar,
    override val isReminderEnabled: Boolean
) : ICreditCardInfo

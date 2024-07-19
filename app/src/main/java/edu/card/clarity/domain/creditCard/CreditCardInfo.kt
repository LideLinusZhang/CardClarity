package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar
import android.os.Parcelable
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CreditCardInfo(
    val id: UUID? = null,
    val name: String,
    val rewardType: RewardType,
    val cardNetworkType: CardNetworkType,
    val statementDate: Calendar,
    val paymentDueDate: Calendar,
    val isReminderEnabled: Boolean,
): Parcelable
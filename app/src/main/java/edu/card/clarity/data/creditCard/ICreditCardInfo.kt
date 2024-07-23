package edu.card.clarity.data.creditCard

import android.icu.util.Calendar
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import java.util.UUID

interface ICreditCardInfo {
    val id: UUID
    val name: String
    val rewardType: RewardType
    val cardNetworkType: CardNetworkType
    val statementDate: Calendar
    val paymentDueDate: Calendar
    val isReminderEnabled: Boolean
}
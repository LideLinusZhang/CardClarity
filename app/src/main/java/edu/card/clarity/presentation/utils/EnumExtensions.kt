package edu.card.clarity.presentation.utils

import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType

internal val RewardType.displayString: String
    get() = when (this) {
        RewardType.CashBack -> "Cash Back"
        RewardType.PointBack -> "Point Back"
    }

internal val RewardType.Companion.displayStrings: List<String>
    get() = RewardType.entries.map { it.displayString }

internal val RewardType.Companion.ordinals: List<Int>
    get() = RewardType.entries.map { it.ordinal }

internal val CardNetworkType.Companion.displayStrings: List<String>
    get() = CardNetworkType.entries.map { it.name }

internal val CardNetworkType.Companion.ordinals: List<Int>
    get() = CardNetworkType.entries.map { it.ordinal }

internal val PurchaseType.Companion.displayStrings: List<String>
    get() = PurchaseType.entries.map { it.name }
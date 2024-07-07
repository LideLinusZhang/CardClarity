package edu.card.clarity.presentation.myCardScreen

import android.os.Parcelable
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditCardFilter(
    val filteredRewardTypes: List<RewardType> = RewardType.entries,
    val filteredCardNetworkTypes: List<CardNetworkType> = CardNetworkType.entries
) : Parcelable {
    fun filter(creditCards: List<CreditCardInfo>): List<CreditCardInfo> {
        return creditCards
            .filter { filteredCardNetworkTypes.contains(it.cardNetworkType) }
            .filter { filteredRewardTypes.contains(it.rewardType) }
    }
}

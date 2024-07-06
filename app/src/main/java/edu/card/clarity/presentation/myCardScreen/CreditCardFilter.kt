package edu.card.clarity.presentation.myCardScreen

import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType

data class CreditCardFilter(
    val rewardType: RewardType? = null,
    val cardNetworkType: CardNetworkType? = null
) {
    fun filter(creditCards: List<CreditCardInfo>): List<CreditCardInfo> {
        return creditCards
            .filter {
                if (rewardType != null) it.rewardType == rewardType else true
            }
            .filter {
                if (cardNetworkType != null) it.cardNetworkType == cardNetworkType else true
            }
    }
}

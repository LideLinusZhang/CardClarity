package edu.card.clarity.data.creditCard

import edu.card.clarity.data.purchaseReward.PurchaseReward

/**
 * A database interface that mirrors [edu.card.clarity.domain.creditCard.ICreditCard],
 * which contains both credit card information and associated purchase rewards.
 */
interface ICreditCard {
    val creditCardInfo: ICreditCardInfo
    val purchaseRewards: List<PurchaseReward>
}
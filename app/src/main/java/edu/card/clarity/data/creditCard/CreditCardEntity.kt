package edu.card.clarity.data.creditCard

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

/**
 * A database relationship data class that mirrors [edu.card.clarity.domain.creditCard.ICreditCard],
 * which contains both credit card information and associated purchase rewards.
 */
data class CreditCardEntity(
    @Embedded val creditCardInfo: CreditCardInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    val purchaseRewards: List<PurchaseRewardEntity>
)

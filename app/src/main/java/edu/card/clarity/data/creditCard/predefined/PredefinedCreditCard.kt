package edu.card.clarity.data.creditCard.predefined

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCard
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

data class PredefinedCreditCard(
    @Embedded override val creditCardInfo: PredefinedCreditCardInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseRewards: List<PurchaseRewardEntity>
): ICreditCard

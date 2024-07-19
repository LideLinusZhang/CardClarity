package edu.card.clarity.data.creditCard.userAdded

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCard
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity

data class UserAddedCreditCard(
    @Embedded override val creditCardInfo: UserAddedCreditCardInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseRewards: List<PurchaseRewardEntity>
): ICreditCard

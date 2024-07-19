package edu.card.clarity.data.creditCard.userAdded

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCard
import edu.card.clarity.data.purchaseReward.PurchaseReward

data class UserAddedCreditCard(
    @Embedded override val creditCardInfo: UserAddedCreditCardInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseRewards: List<PurchaseReward>
): ICreditCard

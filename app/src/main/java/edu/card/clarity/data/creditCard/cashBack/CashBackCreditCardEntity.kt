package edu.card.clarity.data.creditCard.cashBack

import androidx.room.Embedded
import androidx.room.Relation
import edu.card.clarity.data.creditCard.ICreditCardEntity
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity

data class CashBackCreditCardEntity(
    @Embedded override val creditCardInfo: CashBackCreditCardInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "creditCardId"
    )
    override val purchaseReturns: List<MultiplierPurchaseReturnEntity>
) : ICreditCardEntity

package edu.card.clarity.data.purchaseReturn.percentage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardInfoEntity
import edu.card.clarity.data.purchaseReturn.IPurchaseReturnEntity
import edu.card.clarity.domain.PurchaseType
import java.util.UUID

@Entity(
    tableName = "percentagePurchaseReturn",
    primaryKeys = ["creditCardId", "purchaseType"],
    foreignKeys = [
        ForeignKey(
            entity = CashBackCreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("creditCardId")]
)
data class PercentagePurchaseReturnEntity(
    override val creditCardId: UUID,
    override val purchaseType: PurchaseType,
    val percentage: Float
) : IPurchaseReturnEntity

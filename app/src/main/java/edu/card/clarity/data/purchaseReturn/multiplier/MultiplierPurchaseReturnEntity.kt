package edu.card.clarity.data.purchaseReturn.multiplier

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardInfoEntity
import edu.card.clarity.data.purchaseReturn.IPurchaseReturnEntity
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnEntity
import edu.card.clarity.domain.PurchaseType
import java.util.UUID

@Entity(
    tableName = "multiplierPurchaseReturn",
    primaryKeys = ["creditCardId", "purchaseType"],
    foreignKeys = [
        ForeignKey(
            entity = PointBackCreditCardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("creditCardId")]
)
data class MultiplierPurchaseReturnEntity(
    override val creditCardId: UUID,
    override val purchaseType: PurchaseType,
    val multiplier: Float
) : IPurchaseReturnEntity

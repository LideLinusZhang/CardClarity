package edu.card.clarity.data.purchaseReturn.multiplier

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import edu.card.clarity.data.purchaseReturn.IPurchaseReturnEntity
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnEntity
import edu.card.clarity.domain.PurchaseType
import java.util.UUID

@Entity(
    tableName = "multiplierPurchaseReturn",
    primaryKeys = ["creditCardId", "purchaseType"],
    foreignKeys = [
        ForeignKey(
            entity = PercentagePurchaseReturnEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = CASCADE
        )
    ]
)
data class MultiplierPurchaseReturnEntity(
    override val creditCardId: UUID,
    override val purchaseType: PurchaseType,
    val multiplier: Float
) : IPurchaseReturnEntity

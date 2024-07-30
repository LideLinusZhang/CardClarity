package edu.card.clarity.data.purchase.receipt

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.card.clarity.data.purchase.Purchase
import java.util.UUID

@Entity(
    tableName = "receipt",
    foreignKeys = [
        ForeignKey(
            entity = Purchase::class,
            parentColumns = ["id"],
            childColumns = ["purchaseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("purchaseId", unique = true)
    ]
)
data class Receipt(
    @PrimaryKey val purchaseId: UUID,
    val receiptImagePath: String
)

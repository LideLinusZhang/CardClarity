package edu.card.clarity.data.receipt

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "receipts")
data class Receipt(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val date: String,
    val totalAmount: String,
    val merchant: String,
    val selectedCardId: UUID,
    val selectedPurchaseType: String,
    val photoPath: String?
)

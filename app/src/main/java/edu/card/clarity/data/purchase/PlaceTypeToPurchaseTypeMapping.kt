package edu.card.clarity.data.purchase

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.card.clarity.enums.PurchaseType

@Entity(tableName = "placeTypeToPurchaseTypeMapping")
data class PlaceTypeToPurchaseTypeMapping(
    @PrimaryKey val placeType: String,
    val purchaseType: PurchaseType
)

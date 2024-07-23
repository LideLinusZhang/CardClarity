package edu.card.clarity.data.pointSystem

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pointSystem")
data class PointSystem(
    @PrimaryKey val id: UUID,
    val name: String,
    val pointToCashConversionRate: Float
)

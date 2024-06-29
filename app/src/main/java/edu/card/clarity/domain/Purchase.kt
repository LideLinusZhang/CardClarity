package edu.card.clarity.domain

import edu.card.clarity.enums.PurchaseType
import java.util.Date

data class Purchase(
    val time: Date,
    val type: PurchaseType,
    val merchant: String,
    val total: Float
)

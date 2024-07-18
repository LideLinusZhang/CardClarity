package edu.card.clarity.location

import edu.card.clarity.enums.PurchaseType

data class GeolocationInference internal constructor(
    val likelihood: Double,
    val merchantName: String,
    val purchaseType: PurchaseType
)

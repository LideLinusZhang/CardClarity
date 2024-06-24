package edu.card.clarity.domain

import java.util.Date

data class Purchase(
    val time: Date,
    val type: PurchaseType,
    val amount: Float
)

package edu.card.clarity.domain

import edu.card.clarity.domain.purchaseReturn.PurchaseType
import java.util.Date

data class Purchase(
    val time: Date,
    val type: PurchaseType,
    val amount: Float
)

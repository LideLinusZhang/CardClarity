package edu.card.clarity.receiptParsing

import edu.card.clarity.enums.PurchaseType
import java.util.Date

data class ReceiptParsingResult internal constructor(
    val time: Date,
    val total: Float,
    val merchant: String,
    val purchaseType: PurchaseType
)

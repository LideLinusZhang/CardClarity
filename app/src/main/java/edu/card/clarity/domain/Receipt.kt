package edu.card.clarity.domain

import java.util.UUID

data class Receipt(
    val id: UUID? = null,
    val date: String,
    val totalAmount: String,
    val merchant: String,
    val selectedCardId: UUID,
    val selectedPurchaseType: String,
    val photoPath: String?
)
package edu.card.clarity.presentation.myReceiptsScreen

import java.util.UUID

data class ReceiptUiState(
    val id: UUID,
    val purchaseTime: String,
    val merchant: String,
    val purchaseType: String,
    val total: String,
    val creditCardId: UUID,
    val creditCardName: String
)
package edu.card.clarity.presentation.myReceiptsScreen

import java.util.UUID

data class ReceiptsUiState (
    val id: UUID,
    val time: String,
    val merchant: String,
    val type: String,
    val total: String,
    val creditCardId: UUID,
    val creditCard: String
)
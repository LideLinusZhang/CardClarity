package edu.card.clarity.presentation.myCardScreen

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class CreditCardItemUiState(
    val cardName: String,
    val dueDate: String,
    val isReminderEnabled: Boolean,
    val backgroundColor: Color,
    val id: UUID?,
)

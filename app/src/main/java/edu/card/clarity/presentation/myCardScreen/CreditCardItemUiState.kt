package edu.card.clarity.presentation.myCardScreen

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class CreditCardItemUiState(
    val id: UUID,
    val cardName: String,
    val rewardTypeOrdinal: Int,
    val dueDate: String,
    val isReminderEnabled: Boolean,
    val backgroundColor: Color,
)

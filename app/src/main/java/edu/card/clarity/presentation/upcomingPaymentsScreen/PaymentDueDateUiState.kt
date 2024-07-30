package edu.card.clarity.presentation.upcomingPaymentsScreen

import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color

data class PaymentDueDateUiState(
    val cardName: String,
    val dueDate: Calendar,
    val backgroundColor: Color
)
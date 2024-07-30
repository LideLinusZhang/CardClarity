package edu.card.clarity.presentation.upcomingPaymentsScreen

import androidx.compose.ui.graphics.Color
import android.icu.util.Calendar

data class PaymentDueDateUiState(
    val cardName: String,
    val dueDate: Calendar,
    val backgroundColor: Color
)
package edu.card.clarity.presentation.addBenefitScreen

data class AddBenefitScreenUiState(
    val selectedPurchaseType: String,
    val isFactorValid: Boolean,
    val displayedFactor: String
)

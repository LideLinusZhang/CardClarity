package edu.card.clarity.presentation.recordReceiptScreen

data class RecordReceiptUiState(
    val date: String = "",
    val totalAmount: String = "",
    val merchant: String = "",
    val selectedCreditCardName: String? = null,
    val selectedPurchaseType: String? = null,
    val photoPath: String? = null,
    val showCamera: Boolean = false,
    val cameraError: String? = null
)
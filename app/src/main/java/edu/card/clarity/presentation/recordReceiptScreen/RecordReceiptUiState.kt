package edu.card.clarity.presentation.recordReceiptScreen

data class RecordReceiptUiState(
    val date: String = "",
    val total: String = "",
    val merchant: String = "",
    val selectedCreditCardName: String? = null,
    val selectedPurchaseType: String? = null,
    val imagePath: String? = null,
    val showCamera: Boolean = false,
    val cameraError: String? = null
)
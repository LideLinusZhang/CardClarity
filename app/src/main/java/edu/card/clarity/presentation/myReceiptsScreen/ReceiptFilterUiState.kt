package edu.card.clarity.presentation.myReceiptsScreen

data class ReceiptFilterUiState(
    val selectedCreditCardFilterOption: String = ALL_OPTION,
    val selectedPurchaseTypeFilterOption: String = ALL_OPTION
) {
    companion object {
        const val ALL_OPTION: String = "All"
    }
}

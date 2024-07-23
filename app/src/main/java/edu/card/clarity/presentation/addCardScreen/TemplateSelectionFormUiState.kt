package edu.card.clarity.presentation.addCardScreen

data class TemplateSelectionFormUiState(
    val selectedTemplateName: String = SELECT_TEMPLATE_PROMPT,
    val showCardInfo: Boolean = false,
    val cardName: String = "",
    val rewardType: String = "",
    val cardNetworkType: String = "",
    val mostRecentStatementDate: String = "",
    val mostRecentPaymentDueDate: String = "",
    val isReminderEnabled: Boolean = false
) {
    companion object {
        const val SELECT_TEMPLATE_PROMPT: String = "Select a template"
    }
}

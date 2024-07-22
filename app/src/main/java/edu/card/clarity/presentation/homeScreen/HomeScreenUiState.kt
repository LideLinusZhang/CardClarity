package edu.card.clarity.presentation.homeScreen

data class RewardsSummaryItem(
    val month: String,
    val amount: Float
)

data class HomeScreenUiState(
    val rewardsSummary: List<RewardsSummaryItem> = emptyList(),
    val selectedMonths: Int = 1
)
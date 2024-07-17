package edu.card.clarity.presentation.homeScreen

data class RewardsSummaryItem(
    val month: Int,
    val amount: Int
)

data class HomeScreenUiState(
    val rewardsSummary: List<RewardsSummaryItem> = emptyList(),
    val selectedMonths: Int = 3
)
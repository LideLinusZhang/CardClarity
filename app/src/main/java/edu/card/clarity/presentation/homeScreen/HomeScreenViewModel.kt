package edu.card.clarity.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.card.clarity.domain.Purchase
import edu.card.clarity.repositories.PurchaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HomeScreenViewModel(
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        fetchLastMonthsRewards(_uiState.value.selectedMonths)
    }

    fun setSelectedMonths(months: Int) {
        _uiState.update { it.copy(selectedMonths = months) }
        fetchLastMonthsRewards(months)
    }

    private fun fetchLastMonthsRewards(months: Int) {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val endTime = now.time
            now.add(Calendar.MONTH, -months)
            val startTime = now.time

            purchaseRepository.getAllPurchasesStreamBetween(startTime, endTime)
                .map { purchases: List<Purchase> ->
                    val rewardsSummary = calculateRewardsSummary(purchases)
                    HomeScreenUiState(rewardsSummary = rewardsSummary, selectedMonths = months)
                }
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun calculateRewardsSummary(purchases: List<Purchase>): List<RewardsSummaryItem> {
        val rewardsByMonth = mutableMapOf<Int, Int>()

        purchases.forEach { purchase : Purchase ->
            val calendar = Calendar.getInstance().apply {
                time = purchase.time
            }
            val month = calendar.get(Calendar.MONTH)
            val amount = rewardsByMonth[month] ?: 0
            rewardsByMonth[month] = (amount + purchase.total).toInt()
        }

        return rewardsByMonth.entries.map { (month, amount) ->
            RewardsSummaryItem(month = month, amount = amount)
        }.sortedBy { it.month }
    }
}

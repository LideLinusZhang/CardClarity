package edu.card.clarity.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.Purchase
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.repositories.PurchaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

val monthMap = mapOf(
    0 to "Jan",
    1 to "Feb",
    2 to "Mar",
    3 to "Apr",
    4 to "May",
    5 to "Jun",
    6 to "Jul",
    7 to "Aug",
    8 to "Sep",
    9 to "Oct",
    10 to "Nov",
    11 to "Dec"
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
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

    private fun generateDummyPurchasesFlow(startTime: Date, endTime: Date): Flow<List<Purchase>> = flow {
        val dummyPurchases = mutableListOf<Purchase>()
        val calendar = Calendar.getInstance()
        calendar.time = startTime
        var current = calendar.time

        while (current.before(endTime)) {
            val purchase = Purchase(
                id = null,
                time = current,
                merchant = "Dummy Merchant",
                type = PurchaseType.Groceries,
                total = Random.nextFloat() * 100,
                rewardAmount = Random.nextFloat() * 10,
                creditCardId = UUID.randomUUID()
            )
            dummyPurchases.add(purchase)
            calendar.add(Calendar.DAY_OF_MONTH, 10)
            current = calendar.time
        }
        emit(dummyPurchases)
    }


    private fun fetchLastMonthsRewards(months: Int) {
        viewModelScope.launch {
            val now = Calendar.getInstance()
            val endTime = now.time
            now.add(Calendar.MONTH, -(months-1))
            now.set(Calendar.DAY_OF_MONTH, 1)
            val startTime = now.time

            // generate dummy data
            val dummyPurchases = generateDummyPurchasesFlow(startTime, endTime)
            dummyPurchases.map { purchases: List<Purchase> ->
                    val rewardsSummary = calculateRewardsSummary(purchases)
                    HomeScreenUiState(rewardsSummary = rewardsSummary, selectedMonths = months)
                }
                .collect {
                    _uiState.value = it
                }

            // fetch data from repository
//            purchaseRepository.getAllPurchasesStreamBetween(startTime, endTime)
//                .map { purchases: List<Purchase> ->
//                    val rewardsSummary = calculateRewardsSummary(purchases)
//                    HomeScreenUiState(rewardsSummary = rewardsSummary, selectedMonths = months)
//                }
//                .collect {
//                    _uiState.value = it
//                }
        }
    }

    private fun calculateRewardsSummary(purchases: List<Purchase>): List<RewardsSummaryItem> {
        val rewardsByMonth = mutableMapOf<Int, Float>()

        purchases.forEach { purchase : Purchase ->
            val calendar = Calendar.getInstance().apply {
                time = purchase.time
            }
            val month = calendar.get(Calendar.MONTH)
            val amount = rewardsByMonth[month] ?: 0
            rewardsByMonth[month] = (amount.toFloat() + purchase.rewardAmount)
        }

        return rewardsByMonth.entries
            .map { (month, amount) ->
                RewardsSummaryItem(month = monthMap[month] ?: "", amount = amount)
            }
    }
}

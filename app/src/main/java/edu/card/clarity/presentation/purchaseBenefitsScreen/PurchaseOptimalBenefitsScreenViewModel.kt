package edu.card.clarity.presentation.purchaseBenefitsScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseOptimalBenefitsScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _creditCards = MutableStateFlow<List<CreditCardItemUiState>>(emptyList())
    val creditCards: StateFlow<List<CreditCardItemUiState>> = _creditCards

    init {
        val category = savedStateHandle.get<String>("category") ?: ""
        fetchCreditCards(category)
    }

    private fun fetchCreditCards(category: String) {
        viewModelScope.launch {
            cashBackCreditCardRepository.getAllCreditCardsStream().collect { cards ->
                val filteredCards = cards.map { card ->
                    card.toUiState().copy(
                        rewards = card.purchaseRewards.filter { reward ->
                            reward.applicablePurchaseType.toString().equals(category, ignoreCase = true)
                        }.map { reward ->
                            RewardUiState(
                                purchaseType = reward.applicablePurchaseType.toString(),
                                percentage = reward.rewardFactor * 100
                            )
                        }
                    )
                }.filter { it.rewards.isNotEmpty() }

                _creditCards.value = filteredCards
            }
        }
    }

    private fun CashBackCreditCard.toUiState() = CreditCardItemUiState(
        name = this.info.name,
        rewards = this.purchaseRewards.map { reward ->
            RewardUiState(
                purchaseType = reward.applicablePurchaseType.toString(),
                percentage = reward.rewardFactor * 100
            )
        }
    )

    data class CreditCardItemUiState(
        val name: String,
        val rewards: List<RewardUiState>
    )

    data class RewardUiState(
        val purchaseType: String,
        val percentage: Float
    )
}

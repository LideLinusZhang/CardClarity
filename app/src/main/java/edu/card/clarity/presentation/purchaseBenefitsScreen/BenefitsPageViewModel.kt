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
class BenefitsPageViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _creditCards = MutableStateFlow<List<CreditCardItemUiState>>(emptyList())
    val creditCards: StateFlow<List<CreditCardItemUiState>> = _creditCards

    init {
        fetchCreditCards()
    }

    private fun fetchCreditCards() {
        viewModelScope.launch {
            cashBackCreditCardRepository.getAllCreditCardsStream().collect { cards ->
                cards.forEach { card ->
                    Log.d("BenefitsPageViewModel", "Fetched card: $card")
                }
                _creditCards.value = cards.map { it.toUiState() }
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
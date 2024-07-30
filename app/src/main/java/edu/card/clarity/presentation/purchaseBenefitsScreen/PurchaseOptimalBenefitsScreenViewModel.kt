package edu.card.clarity.presentation.purchaseBenefitsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseOptimalBenefitsScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val category: PurchaseType =
        PurchaseType.valueOf(savedStateHandle.get<String>("category") ?: "")

    private val _creditCards = MutableStateFlow<List<CreditCardItemUiState>>(emptyList())
    val creditCards: StateFlow<List<CreditCardItemUiState>> = _creditCards

    private val _optimalCreditCard = MutableStateFlow<CreditCardItemUiState?>(null)
    val optimalCreditCard: StateFlow<CreditCardItemUiState?> = _optimalCreditCard

    private val _optimalCardMessage = MutableStateFlow<String?>(null)
    val optimalCardMessage: StateFlow<String?> = _optimalCardMessage

    init {
        fetchCreditCards(category)
        findOptimalCreditCard(category)
    }

    private fun fetchCreditCards(category: PurchaseType) {
        viewModelScope.launch {
            val cashBackCardsFlow =
                cashBackCreditCardRepository.getAllCreditCardsStream().map { cards ->
                    cards.flatMap { card ->
                        val rewardsForCategory = card.purchaseRewards.filter { reward ->
                            reward.applicablePurchaseType == category
                        }

                        val rewards = rewardsForCategory.ifEmpty {
                            card.purchaseRewards.filter { reward ->
                                reward.applicablePurchaseType == PurchaseType.Others
                            }
                        }

                        rewards.map { reward ->
                            CreditCardItemUiState(
                                name = card.info.name,
                                rewards = listOf(
                                    RewardUiState(
                                        purchaseType = reward.applicablePurchaseType.name,
                                        description = "${(reward.rewardFactor * 100).toInt()}% Cashback"
                                    )
                                )
                            )
                        }
                    }
                }

            val pointBackCardsFlow =
                pointBackCreditCardRepository.getAllCreditCardsStream().map { cards ->
                    cards.flatMap { card ->
                        val rewardsForCategory = card.purchaseRewards.filter { reward ->
                            reward.applicablePurchaseType == category
                        }

                        val rewards = rewardsForCategory.ifEmpty {
                            card.purchaseRewards.filter { reward ->
                                reward.applicablePurchaseType == PurchaseType.Others
                            }
                        }

                        rewards.map { reward ->
                            CreditCardItemUiState(
                                name = card.info.name,
                                rewards = listOf(
                                    RewardUiState(
                                        purchaseType = reward.applicablePurchaseType.name,
                                        description = "${reward.rewardFactor}x Points"
                                    )
                                )
                            )
                        }
                    }
                }

            combine(cashBackCardsFlow, pointBackCardsFlow) { cashBackCards, pointBackCards ->
                cashBackCards + pointBackCards
            }.collect { combinedCards ->
                _creditCards.value = combinedCards
            }
        }
    }

    private fun findOptimalCreditCard(category: PurchaseType) {
        viewModelScope.launch {
            val dummyTotal = 100.0f

            val optimalCashBackCard = try {
                cashBackCreditCardRepository.findOptimalCreditCard(dummyTotal, category)
            } catch (e: NoSuchElementException) {
                null
            }
            val optimalPointBackCard = try {
                pointBackCreditCardRepository.findOptimalCreditCard(dummyTotal, category)
            } catch (e: NoSuchElementException) {
                null
            }

            val optimalCard = listOfNotNull(optimalCashBackCard, optimalPointBackCard).maxByOrNull {
                it.getReturnAmountInCash(dummyTotal, category)
            }

            // The Optimal Card's corresponding benefit that makes it optimal
            optimalCard?.let { card ->
                val rewardsForCategory = card.purchaseRewards.filter { reward ->
                    reward.applicablePurchaseType == category
                }

                val rewards = rewardsForCategory.ifEmpty {
                    card.purchaseRewards.filter { reward ->
                        reward.applicablePurchaseType == PurchaseType.Others
                    }
                }

                val uiState = CreditCardItemUiState(
                    name = card.info.name,
                    rewards = rewards.map { reward ->
                        RewardUiState(
                            purchaseType = reward.applicablePurchaseType.name,
                            description = if (card.info.rewardType == RewardType.CashBack) {
                                "${(reward.rewardFactor * 100).toInt()}% Cashback"
                            } else {
                                "${reward.rewardFactor}x Points"
                            }
                        )
                    }
                )
                _optimalCreditCard.value = uiState
            }
        }
    }

    data class CreditCardItemUiState(
        val name: String,
        val rewards: List<RewardUiState>
    )

    data class RewardUiState(
        val purchaseType: String,
        val description: String
    )
}

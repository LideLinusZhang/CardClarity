package edu.card.clarity.presentation.addBenefitScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddBenefitScreenViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val cardId: UUID = savedStateHandle["cardId"]!!
    private val cardRewardType: RewardType = savedStateHandle["cardRewardType"]!!

    val purchaseTypeOptionStrings = PurchaseType.entries.map { it.name }

    private var selectedPurchaseType: PurchaseType = PurchaseType.entries.first()
    private var isFactorValid: Boolean = true
    private var factor: Float? = null

    private val _uiState = MutableStateFlow(
        AddBenefitScreenUiState(
            selectedPurchaseType = purchaseTypeOptionStrings.first(),
            isFactorValid = true,
            factor = ""
        )
    )
    val uiState: StateFlow<AddBenefitScreenUiState> = _uiState.asStateFlow()

    fun updateSelectedPurchaseType(index: Int) {
        selectedPurchaseType = PurchaseType.entries[index]

        _uiState.update {
            it.copy(selectedPurchaseType = purchaseTypeOptionStrings[index])
        }
    }

    fun updateFactor(factorInString: String) {
        factor = factorInString.toFloatOrNull()
        isFactorValid = when (cardRewardType) {
            RewardType.CashBack -> factor?.let { percentage ->
                percentage > 0.0 && percentage <= 100.0
            } ?: false

            RewardType.PointBack -> factor?.let { multiplier ->
                multiplier >= 1.0
            } ?: false
        }

        _uiState.update {
            it.copy(
                isFactorValid = isFactorValid,
                factor = factorInString
            )
        }
    }

    fun addBenefit() {
        viewModelScope.launch {
            if (cardRewardType == RewardType.CashBack) {
                cashBackCreditCardRepository.addPurchaseReward(
                    cardId,
                    listOf(selectedPurchaseType),
                    factor!!
                )
                Log.d(
                    "MyBenefitsScreenVM",
                    "New benefit added: ${selectedPurchaseType.name} - ${(factor!! * 100).toInt()}% cashback"
                )
            }
        }
    }
}
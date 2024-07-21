package edu.card.clarity.presentation.addCardScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
) : ViewModel() {

    private val _templates = MutableStateFlow<List<CreditCardInfo>>(emptyList())
    val templates: StateFlow<List<CreditCardInfo>> = _templates

    init {
        fetchTemplates()
    }

    private fun fetchTemplates() {
        viewModelScope.launch {
            try {
                val cashBackTemplatesFlow = cashBackCreditCardRepository.getAllPredefinedCreditCardsStream()
                    .map { cards ->
                        cards.map { it.info }
                    }
                cashBackTemplatesFlow.collect { cashBackTemplates ->
                    _templates.value = cashBackTemplates
                }
            } catch (e: Exception) {
                Log.e("AddCardViewModel", "Error fetching templates", e)
            }
        }
    }
}

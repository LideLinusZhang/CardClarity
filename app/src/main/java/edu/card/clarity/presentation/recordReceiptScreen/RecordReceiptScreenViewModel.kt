package edu.card.clarity.presentation.recordReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import edu.card.clarity.domain.creditCard.CreditCardInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordReceiptViewModel @Inject constructor(
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordReceiptUiState())
    val uiState: StateFlow<RecordReceiptUiState> = _uiState.asStateFlow()

    private val _allCards = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }.stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    val allCards: StateFlow<List<CreditCardInfo>> = _allCards

    fun onCameraError(error: String) {
        _uiState.value = _uiState.value.copy(cameraError = error)
    }

    fun resetCameraError() {
        _uiState.value = _uiState.value.copy(cameraError = null)
    }

    fun onImageCaptured(imagePath: String) {
        _uiState.value = _uiState.value.copy(photoPath = imagePath, showCamera = false)
    }

    fun openCamera() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun scanReceipt() {
        // TOOD: Implement receipt scanning logic
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                date = "2024-07-20",
                totalAmount = "45.99",
                merchant = "Local Store"
            )
        }
    }

    fun onDateChange(newDate: String) {
        _uiState.value = _uiState.value.copy(date = newDate)
    }

    fun onTotalAmountChange(newTotalAmount: String) {
        _uiState.value = _uiState.value.copy(totalAmount = newTotalAmount)
    }

    fun onMerchantChange(newMerchant: String) {
        _uiState.value = _uiState.value.copy(merchant = newMerchant)
    }

    fun onCardSelected(newCard: String) {
        _uiState.value = _uiState.value.copy(selectedCard = newCard)
    }

    fun onPurchaseTypeSelected(newPurchaseType: String) {
        _uiState.value = _uiState.value.copy(selectedPurchaseType = newPurchaseType)
    }

    fun addReceipt() {
        // TOOD: Save Receipt
        viewModelScope.launch {
        }
    }
}

data class RecordReceiptUiState(
    val date: String = "",
    val totalAmount: String = "",
    val merchant: String = "",
    val selectedCard: String = "",
    val selectedPurchaseType: String = "",
    val photoPath: String? = null,
    val showCamera: Boolean = false,
    val cameraError: String? = null
)

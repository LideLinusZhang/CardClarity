package edu.card.clarity.presentation.recordReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordReceiptViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RecordReceiptUiState())
    val uiState: StateFlow<RecordReceiptUiState> = _uiState

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    fun onImageCaptured(imagePath: String) {
        _uiState.value = _uiState.value.copy(photoPath = imagePath)
        _showCamera.value = false
    }

    fun openCamera() {
        _showCamera.value = true
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
    val photoPath: String? = null
)

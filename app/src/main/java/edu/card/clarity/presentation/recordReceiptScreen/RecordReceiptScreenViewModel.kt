package edu.card.clarity.presentation.recordReceiptScreen

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.Purchase
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.receiptParsing.ReceiptParser
import edu.card.clarity.repositories.PurchaseRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordReceiptScreenViewModel @Inject constructor(
    cashBackCreditCardRepository: CashBackCreditCardRepository,
    pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val purchaseRepository: PurchaseRepository,
    private val receiptParser: ReceiptParser,
) : ViewModel() {
    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private var selectedCreditCardIndex: Int? = null
    private var selectedPurchaseType: PurchaseType? = null

    private val _uiState = MutableStateFlow(RecordReceiptUiState())
    val uiState: StateFlow<RecordReceiptUiState> = _uiState.asStateFlow()

    private val allCard = combine(
        cashBackCreditCardRepository.getAllCreditCardsStream(),
        pointBackCreditCardRepository.getAllCreditCardsStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }
        .stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val allCardNames: StateFlow<List<String>> = allCard
        .mapLatest { it.map { card -> card.info.name } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onCameraError(error: String) {
        _uiState.value = _uiState.value.copy(cameraError = error)
    }

    fun resetCameraError() {
        _uiState.value = _uiState.value.copy(cameraError = null)
    }

    fun onImageCaptured(imagePath: String) {
        _uiState.value = _uiState.value.copy(imagePath = imagePath, showCamera = false)
        _uiState.value = _uiState.value.copy(
            imagePath = imagePath,
            showCamera = false
        )

        viewModelScope.launch {
            val parseResult = receiptParser.parseReceiptImage(imagePath)

            selectedPurchaseType = parseResult.purchaseType

            _uiState.value = _uiState.value.copy(
                date = dateFormatter.format(parseResult.time),
                total = parseResult.total.toString(),
                merchant = parseResult.merchant,
                selectedPurchaseType = parseResult.purchaseType.name
            )
        }
    }

    fun openCamera() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun updateTotalAmount(totalAmount: String) {
        _uiState.value = _uiState.value.copy(total = totalAmount)
    }

    fun updateMerchant(merchant: String) {
        _uiState.value = _uiState.value.copy(merchant = merchant)
    }

    fun updateSelectedCreditCard(selectedCreditCardIndex: Int) {
        this.selectedCreditCardIndex = selectedCreditCardIndex
        _uiState.value = _uiState.value.copy(
            selectedCreditCardName = allCardNames.value[selectedCreditCardIndex]
        )
    }

    fun updateSelectedPurchaseType(selectedPurchaseTypeIndex: Int) {
        selectedPurchaseType = PurchaseType.entries[selectedPurchaseTypeIndex]
        _uiState.value = _uiState.value.copy(
            selectedPurchaseType = selectedPurchaseType!!.name
        )
    }

    fun addReceipt() {
        viewModelScope.launch {
            if (selectedCreditCardIndex != null && selectedCreditCardIndex!! < allCard.value.size) {
                val creditCard = allCard.value[selectedCreditCardIndex!!]
                val total = _uiState.value.total.toFloat()
                val type = selectedPurchaseType!!
                val time = dateFormatter.parse(_uiState.value.date)

                val purchaseId = purchaseRepository.addPurchase(
                    Purchase(
                        time = time,
                        total = total,
                        merchant = _uiState.value.merchant,
                        type = type,
                        rewardAmount = creditCard.getReturnAmountInCash(total, type),
                        creditCardId = creditCard.info.id!!
                    )
                )

                uiState.value.imagePath?.let {
                    purchaseRepository.addReceiptImagePath(it, purchaseId)
                }
            }
        }
    }
}


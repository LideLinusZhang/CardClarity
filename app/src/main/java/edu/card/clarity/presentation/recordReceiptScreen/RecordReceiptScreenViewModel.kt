package edu.card.clarity.presentation.recordReceiptScreen

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import edu.card.clarity.domain.Purchase
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.utils.scanReceipt
import edu.card.clarity.repositories.PurchaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.json.JSONObject

@HiltViewModel
class RecordReceiptScreenViewModel @Inject constructor(
    cashBackCreditCardRepository: CashBackCreditCardRepository,
    pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val purchaseRepository: PurchaseRepository,
    @ApplicationContext private val context: Context,
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

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    fun onCameraError(error: String) {
        _uiState.value = _uiState.value.copy(cameraError = error)
    }

    fun resetCameraError() {
        _uiState.value = _uiState.value.copy(cameraError = null)
    }

    fun onImageCaptured(imagePath: String) {
        _uiState.value = _uiState.value.copy(photoPath = imagePath, showCamera = false)

        viewModelScope.launch {
            try {
                val response = scanReceipt(context, imagePath)
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val jsonObject = JSONObject(responseBody)
                        processReceiptData(jsonObject)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun processReceiptData(jsonObject: JSONObject) {
        val createdDate = jsonObject.optString("created_date")
        val totalAmount = jsonObject.optString("total")
        val merchant = jsonObject.optJSONObject("vendor")?.optString("name")
        val purchaseType = jsonObject.optString("category")

        _uiState.value = _uiState.value.copy(
            date = createdDate,
            totalAmount = totalAmount,
            merchant = merchant ?: "",
            selectedPurchaseType = purchaseType
        )
    }

    fun openCamera() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun resetCamera() {
        _showCamera.value = true
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun updateTotalAmount(totalAmount: String) {
        _uiState.value = _uiState.value.copy(totalAmount = totalAmount)
    }

    fun updateMerchant(merchant: String) {
        _uiState.value = _uiState.value.copy(merchant = merchant)
    }

    fun updateSelectedCreditCard(selectedCreditCardIndex: Int) {
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
            val creditCard = allCard.value[selectedCreditCardIndex!!]
            val total = _uiState.value.totalAmount.toFloat()
            val type = selectedPurchaseType!!

            val purchaseId = purchaseRepository.addPurchase(
                Purchase(
                    time = dateFormatter.parse(_uiState.value.date),
                    total = total,
                    merchant = _uiState.value.merchant,
                    type = type,
                    rewardAmount = creditCard.getReturnAmountInCash(total, type),
                    creditCardId = creditCard.info.id!!
                )
            )

            uiState.value.photoPath?.let {
                purchaseRepository.addReceiptImagePath(it, purchaseId)
            }
        }
    }
}


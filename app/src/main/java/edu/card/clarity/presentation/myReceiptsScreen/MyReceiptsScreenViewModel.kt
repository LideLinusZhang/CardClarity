package edu.card.clarity.presentation.myReceiptsScreen

import android.icu.text.DateFormat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayStrings
import edu.card.clarity.repositories.PurchaseRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyReceiptsScreenViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository,
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dateFormatter: DateFormat
) : ViewModel() {
    private val savedCreditCardFilter: StateFlow<ReceiptFilter> = savedStateHandle
        .getStateFlow(
            key = MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY,
            initialValue = ReceiptFilter()
        )

    val receipts: StateFlow<List<ReceiptUiState>> = combine(
        purchaseRepository.getAllPurchasesStream(),
        savedCreditCardFilter
    ) { receipts, filter ->
        receipts
            .filter {
                if (filter.filteredPurchaseType != null)
                    it.type == filter.filteredPurchaseType
                else true
            }
            .filter {
                if (filter.filteredCreditCardId != null)
                    it.creditCardId == filter.filteredCreditCardId
                else true
            }
    }
        .map { it.map { receipt -> receipt.toUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    private val _receiptFilterUiState = MutableStateFlow(
        ReceiptFilterUiState()
    )

    val receiptFilterUiState = _receiptFilterUiState.asStateFlow()

    private val creditCards: StateFlow<List<CreditCardInfo>> = combine(
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    ) { cashBack, pointBack ->
        cashBack + pointBack
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val creditCardFilterOptionStrings: StateFlow<List<String>> = creditCards
        .mapLatest {
            listOf(ReceiptFilterUiState.ALL_OPTION) + it.map { creditCard -> creditCard.name }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val purchaseTypeFilterOptionStrings =
        listOf(ReceiptFilterUiState.ALL_OPTION) + PurchaseType.displayStrings

    fun setCreditCardFilter(optionIndex: Int) {
        savedStateHandle[MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY] = savedCreditCardFilter
            .value
            .copy(filteredCreditCardId = if (optionIndex == 0) null else creditCards.value[optionIndex - 1].id)

        _receiptFilterUiState.value = _receiptFilterUiState
            .value
            .copy(selectedCreditCardFilterOption = creditCards.value[optionIndex - 1].name)
    }

    fun setPurchaseTypeFilter(optionIndex: Int) {
        savedStateHandle[MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY] = savedCreditCardFilter
            .value
            .copy(filteredPurchaseType = if (optionIndex == 0) null else PurchaseType.entries[optionIndex - 1])

        _receiptFilterUiState.value = _receiptFilterUiState
            .value
            .copy(selectedPurchaseTypeFilterOption = purchaseTypeFilterOptionStrings[optionIndex])
    }

    fun deleteReceipt(id: UUID) = viewModelScope.launch {
        val imagePath = purchaseRepository.getReceiptImagePathById(id)

        withContext(Dispatchers.IO) {
            if (imagePath != null) {
                val imageFile = File(imagePath)

                imageFile.delete()
            }
        }

        purchaseRepository.removePurchase(id)
    }

    private suspend fun Purchase.toUiState() = ReceiptUiState(
        id = this.id!!,
        purchaseTime = dateFormatter.format(this.time),
        merchant = this.merchant,
        total = this.total.toString(),
        purchaseType = this.type.name,
        creditCardId = this.creditCardId,
        creditCardName = getCreditCardName(this.creditCardId),
        receiptImagePath = getReceiptImagePath(this.id)
    )

    private suspend fun getCreditCardName(id: UUID): String {
        val creditCardInfo = cashBackCreditCardRepository.getCreditCardInfo(id)
            ?: pointBackCreditCardRepository.getCreditCardInfo(id)

        // Credit Card UUIDs here are directly fetched from DB, where foreign key
        // constraints make sure these UUIDs are present in the DB.
        return creditCardInfo?.name!!
    }

    private suspend fun getReceiptImagePath(id: UUID): String? {
        return purchaseRepository.getReceiptImagePathById(id)
    }

    private companion object {
        private const val MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY: String =
            "MY_RECEIPTS_SCREEN_SAVED_FILTER"
    }
}
package edu.card.clarity.presentation.myReceiptsScreen

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.data.receipt.ReceiptDao
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.Receipt
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayStrings
import edu.card.clarity.repositories.PurchaseRepository
import edu.card.clarity.repositories.ReceiptRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyReceiptsScreenViewModel @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private val savedCreditCardFilter: StateFlow<ReceiptFilter> = savedStateHandle
        .getStateFlow(
            key = MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY,
            initialValue = ReceiptFilter()
        )

    val receipts: StateFlow<List<ReceiptUiState>> = combine(
        receiptRepository.getAllReceiptsStream(),
        savedCreditCardFilter
    ) { receipts, filter ->
        receipts
            .filter {
                if (filter.filteredPurchaseType != null)
                    it.selectedPurchaseType == filter.filteredPurchaseType.toString()
                else true
            }
            .filter {
                if (filter.filteredCreditCardId != null)
                    it.selectedCardId == filter.filteredCreditCardId
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
        receiptRepository.removeReceipt(id)
    }

    private suspend fun Receipt.toUiState() = ReceiptUiState(
        id = this.id!!,
        purchaseTime = this.date,
        merchant = this.merchant,
        total = this.totalAmount,
        purchaseType = this.selectedPurchaseType,
        creditCardId = this.selectedCardId,
        creditCardName = getCreditCardName(this.selectedCardId),
        photoPath = this.photoPath
    )

    private suspend fun getCreditCardName(id: UUID): String {
        val creditCardInfo = cashBackCreditCardRepository.getCreditCardInfo(id)
            ?: pointBackCreditCardRepository.getCreditCardInfo(id)

        // Credit Card UUIDs here are directly fetched from DB, where foreign key
        // constraints make sure these UUIDs are present in the DB.
        return creditCardInfo?.name!!
    }

    companion object {
        private const val MY_RECEIPTS_SCREEN_SAVED_FILTER_KEY: String =
            "MY_RECEIPTS_SCREEN_SAVED_FILTER"
    }
}
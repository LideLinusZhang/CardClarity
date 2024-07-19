package edu.card.clarity.presentation.myReceiptsScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.utils.WhileUiSubscribed
import edu.card.clarity.presentation.utils.displayStrings
import edu.card.clarity.repositories.PurchaseRepository
import edu.card.clarity.repositories.creditCard.CashBackCreditCardRepository
import edu.card.clarity.repositories.creditCard.PointBackCreditCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyReceiptsScreenViewModel @Inject constructor (
    private val receiptRepository: PurchaseRepository,
    private val cashBackCreditCardRepository: CashBackCreditCardRepository,
    private val pointBackCreditCardRepository: PointBackCreditCardRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_SELECTED_CARD_FILTER = "selected_card_filter"
        private const val KEY_SELECTED_PURCHASE_TYPE_FILTER = "selected_purchase_type_filter"
        private val dateFormatter = SimpleDateFormat.getDateInstance()
    }

    private val allCardsOption = flow<List<CreditCardInfo>> {
        emit(
            listOf(
                CreditCardInfo(
                    null, "All", RewardType.CashBack,
                    CardNetworkType.MasterCard,
                    Calendar.getInstance(),
                    Calendar.getInstance(), false
                )
            )
        )
    }

    /*// Temporary Data
    private val creditCards = listOf(
        CreditCardInfo(
            UUID.randomUUID(), "Visa Dividend", RewardType.CashBack,
            CardNetworkType.MasterCard,
            Calendar.getInstance(),
            Calendar.getInstance(), false
        ),
        CreditCardInfo(
            UUID.randomUUID(), "MasterCard", RewardType.CashBack,
            CardNetworkType.MasterCard,
            Calendar.getInstance(),
            Calendar.getInstance(), false
        )
    )
    private val receipts: StateFlow<List<ReceiptsUiState>> = MutableStateFlow(
        listOf(
            ReceiptsUiState(
                UUID.randomUUID(),
                "2024-05-19",
                "Shell",
                "Gas",
                "30.22",
                creditCards[0].id!!,
                creditCards[0].name
            ),
            ReceiptsUiState(
                UUID.randomUUID(),
                "2024-06-06",
                "Air Canada",
                "Travel",
                "520.87",
                creditCards[1].id!!,
                creditCards[1].name
            ),
            ReceiptsUiState(
                UUID.randomUUID(),
                "2024-07-21",
                "Hilton",
                "Hotel",
                "837.25",
                creditCards[1].id!!,
                creditCards[1].name
            )
        )
    )
    val cards = combine(
        allCardsOption, flow<List<CreditCardInfo>> { emit(creditCards) }
    ) { list1, list2 ->
        list1 + list2
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = listOf()
    )*/

    private val receipts = receiptRepository.getAllPurchasesStream().map { receipts ->
        receipts.map {
            ReceiptsUiState(
                it.id!!,
                dateFormatter.format(it.time),
                it.merchant,
                it.total.toString(),
                it.type.toString(),
                it.creditCardId,
                getCardName(it.creditCardId)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = listOf()
    )

    val cards = combine(
        allCardsOption,
        cashBackCreditCardRepository.getAllCreditCardInfoStream(),
        pointBackCreditCardRepository.getAllCreditCardInfoStream()
    )
    { list1, list2 , list3->
        list1 + list2 + list3
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = listOf()
    )

    val purchaseTypeOptions = PurchaseType.displayStrings

    private val _selectedCardFilter =
        MutableStateFlow(savedStateHandle.get<CreditCardInfo?>(KEY_SELECTED_CARD_FILTER))
    val selectedCardFilter: StateFlow<CreditCardInfo?> = _selectedCardFilter

    private val _selectedPurchaseTypeFilter =
        MutableStateFlow(savedStateHandle.get<String?>(KEY_SELECTED_PURCHASE_TYPE_FILTER))
    val selectedPurchaseTypeFilter: StateFlow<String?> = _selectedPurchaseTypeFilter

    val filteredReceipts: StateFlow<List<ReceiptsUiState>> = combine(
        receipts, selectedCardFilter, selectedPurchaseTypeFilter
    ) { receipts, cardFilter, purchaseTypeFilter ->
        var filtered = receipts
        if (cardFilter?.id != null) {
            cardFilter.let {
                filtered = filtered.filter { it.creditCardId == cardFilter.id }
            }
        }
        if (purchaseTypeFilter != "All") {
            purchaseTypeFilter?.let {
                filtered = filtered.filter { it.type == purchaseTypeFilter }
            }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private suspend fun getCardName(id: UUID): String {
        val card = cashBackCreditCardRepository.getCreditCardInfo(id)
            ?: pointBackCreditCardRepository.getCreditCardInfo(id)
        return card!!.name
    }

    fun setCardFilter(card: CreditCardInfo) {
        _selectedCardFilter.value = card
        savedStateHandle[KEY_SELECTED_CARD_FILTER] = card
    }

    fun setPurchaseTypeFilter(purchaseType: String) {
        _selectedPurchaseTypeFilter.value = purchaseType
        savedStateHandle[KEY_SELECTED_PURCHASE_TYPE_FILTER] = purchaseType
    }

    fun deleteReceipt(id: UUID) = viewModelScope.launch {
        receiptRepository.removePurchase(id)
    }
}
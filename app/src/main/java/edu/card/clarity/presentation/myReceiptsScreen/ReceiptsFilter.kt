package edu.card.clarity.presentation.myReceiptsScreen


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.card.clarity.presentation.common.DropdownMenu

@Composable
fun ReceiptsFilter(
    viewModel: MyReceiptsScreenViewModel = hiltViewModel()
) {
    val creditCards by viewModel.cards.collectAsStateWithLifecycle()
    val selectedCardFilter by viewModel.selectedCardFilter.collectAsStateWithLifecycle()
    val selectedPurchaseTypeFilter by viewModel.selectedPurchaseTypeFilter.collectAsStateWithLifecycle()


    DropdownMenu(
        label = "Credit Card",
        options = creditCards.map { if (it.name == "") "Unnamed Card" else it.name },
        selectedOption = if (selectedCardFilter?.name == "") "Unnamed Card" else selectedCardFilter?.name ?: "All"
    ) { index ->
        viewModel.setCardFilter(creditCards[index])
    }

    Spacer(modifier = Modifier.height(8.dp))

    val purchaseTypeOptions = listOf("All") + viewModel.purchaseTypeOptions

    DropdownMenu(
        label = "Purchase Type",
        options = purchaseTypeOptions,
        selectedOption = selectedPurchaseTypeFilter ?: "All"
    ) { index ->
        viewModel.setPurchaseTypeFilter(purchaseTypeOptions[index])
    }

    Spacer(modifier = Modifier.height(16.dp))
}
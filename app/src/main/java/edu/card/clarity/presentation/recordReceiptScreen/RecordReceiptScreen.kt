package edu.card.clarity.presentation.recordReceiptScreen

<<<<<<< HEAD
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.presentation.common.TextField
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.common.DatePickerField
import java.util.Calendar

@Composable
fun RecordReceiptScreen(viewModel: RecordReceiptViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.onDateChange("$year-${month + 1}-$dayOfMonth")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "Record a Receipt",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn {
                item {
                    Button(
                        onClick = viewModel::scanReceipt,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Scan your receipt")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text("Detected information:")
                }
                item {
                    DatePickerField(
                        date = uiState.date,
                        label = "Date",
                        onClick = { datePickerDialog.show() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    TextField(
                        label = "Total Amount",
                        text = uiState.totalAmount,
                        placeholderText = "Enter total amount",
                        onTextChange = viewModel::onTotalAmountChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    TextField(
                        label = "Merchant",
                        text = uiState.merchant,
                        placeholderText = "Enter merchant",
                        onTextChange = viewModel::onMerchantChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    DropdownMenu(
                        label = "Card Type",
                        options = CardNetworkType.entries.map { it.name },
                        selectedOption = uiState.selectedCard,
                        onOptionSelected = { viewModel.onCardSelected(CardNetworkType.entries[it].name) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    DropdownMenu(
                        label = "Purchase Type",
                        options = PurchaseType.entries.map { it.name },
                        selectedOption = uiState.selectedPurchaseType,
                        onOptionSelected = { viewModel.onPurchaseTypeSelected(PurchaseType.entries[it].name) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Button(
                        onClick = viewModel::addReceipt,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Receipt")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordReceiptScreenPreview() {
    // mock data for preview
    val mockViewModel = RecordReceiptViewModel().apply {
        onDateChange("2024-07-20")
        onTotalAmountChange("45.99")
        onMerchantChange("Walmart")
        onCardSelected("Visa")
        onPurchaseTypeSelected("Groceries")
    }

    RecordReceiptScreen(viewModel = mockViewModel)
}
=======
>>>>>>> e5294ad (update manifest permissions)

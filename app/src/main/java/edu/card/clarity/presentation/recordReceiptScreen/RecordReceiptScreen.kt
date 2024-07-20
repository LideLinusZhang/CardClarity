package edu.card.clarity.presentation.recordReceiptScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.enums.PurchaseType


@Composable
fun RecordReceiptScreen(viewModel: RecordReceiptViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    OutlinedTextField(
                        value = uiState.date,
                        onValueChange = viewModel::onDateChange,
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = uiState.totalAmount,
                        onValueChange = viewModel::onTotalAmountChange,
                        label = { Text("Total Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    OutlinedTextField(
                        value = uiState.merchant,
                        onValueChange = viewModel::onMerchantChange,
                        label = { Text("Merchant") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    DropdownMenuCardSelector(
                        selectedCard = uiState.selectedCard,
                        onCardSelected = viewModel::onCardSelected
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    DropdownMenuPurchaseType(
                        selectedPurchaseType = uiState.selectedPurchaseType,
                        onPurchaseTypeSelected = viewModel::onPurchaseTypeSelected
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCardSelector(selectedCard: String, onCardSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val cardOptions = listOf("Visa", "MasterCard", "Amex") // Add more card options here

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedCard,
            onValueChange = { },
            readOnly = true,
            label = { Text("Select Card") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cardOptions.forEach { card ->
                DropdownMenuItem(
                    text = { Text(card) },
                    onClick = {
                        onCardSelected(card)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuPurchaseType(selectedPurchaseType: String, onPurchaseTypeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val purchaseTypes = PurchaseType.entries.map { it.name }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedPurchaseType,
            onValueChange = { },
            readOnly = true,
            label = { Text("Select Purchase Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            purchaseTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onPurchaseTypeSelected(type)
                        expanded = false
                    }
                )
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

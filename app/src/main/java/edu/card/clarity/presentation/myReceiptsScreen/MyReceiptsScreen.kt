package edu.card.clarity.presentation.myReceiptsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.presentation.common.CustomButton
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.utils.Destinations
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue

@Composable
fun MyReceiptsScreen(navController: NavController,
                     viewModel: MyReceiptsScreenViewModel = hiltViewModel()) {
    val receipts by viewModel.receipts.collectAsState()
    val receiptFilterUiState by viewModel.receiptFilterUiState.collectAsState()
    val creditCardFilterOptions by viewModel.creditCardFilterOptionStrings.collectAsState()

    CardClarityTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Shopping Icon", tint = DarkAccentBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "My Receipts",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = CardClarityTypography.titleLarge,
                        color = Color.Black
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DropdownMenu(
                    label = "Credit Card",
                    options = creditCardFilterOptions,
                    selectedOption = receiptFilterUiState.selectedCreditCardFilterOption,
                    onOptionSelected = viewModel::setCreditCardFilter
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenu(
                    label = "Purchase Type",
                    options = viewModel.purchaseTypeFilterOptionStrings,
                    selectedOption = receiptFilterUiState.selectedPurchaseTypeFilterOption,
                    onOptionSelected = viewModel::setPurchaseTypeFilter
                )
            }
            items(receipts) { receipt ->
                ReceiptsItem(receipt, viewModel::deleteReceipt)
            }
            item {
                CustomButton(
                    text = "Record a Receipt",
                    onClick = {
                        navController.navigate(Destinations.RECORD_RECEIPT)
                    }
                )
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
@Preview
fun MyReceiptsScreenPreview() {
    val navController = rememberNavController()
    MyReceiptsScreen(navController)
}
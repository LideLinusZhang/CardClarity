package edu.card.clarity.presentation.myReceiptsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

@Composable
fun MyReceiptsScreen(viewModel: MyReceiptsScreenViewModel = hiltViewModel()) {
    val receipts by viewModel.receipts.collectAsState()
    val receiptFilterUiState by viewModel.receiptFilterUiState.collectAsState()
    val creditCardFilterOptions by viewModel.creditCardFilterOptionStrings.collectAsState()

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "My Receipts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(0.5f)) {
                LazyColumn {
                    items(receipts.size) { index ->
                        ReceiptsItem(receipts[index], viewModel::deleteReceipt)
                    }
                }
            }

            Button(
                onClick = { /* TODO: Handle record receipt action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White,
                    contentColor = Color.Black),
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(25),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Record a Receipt")
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
@Preview
fun MyReceiptsScreenPreview() {
    MyReceiptsScreen()
}
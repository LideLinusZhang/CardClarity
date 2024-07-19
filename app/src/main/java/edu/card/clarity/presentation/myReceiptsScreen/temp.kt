package edu.card.clarity.presentation.myReceiptsScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.util.UUID


@Composable
fun MyReceiptsScreen() {
    val cardFilterOptions = listOf("Visa Dividend", "MasterCard", "American Express")
    val purchaseTypeOptions = listOf("Gas", "Groceries", "Electronics")

    var selectedCardFilter by remember { mutableStateOf(cardFilterOptions[0]) }
    var selectedPurchaseType by remember { mutableStateOf(purchaseTypeOptions[0]) }
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
                options = cardFilterOptions,
                selectedOption = selectedCardFilter
            ) { index ->
                selectedCardFilter = cardFilterOptions[index]
            }

            Spacer(modifier = Modifier.height(8.dp))

            DropdownMenu(
                label = "Purchase Type",
                options = purchaseTypeOptions,
                selectedOption = selectedPurchaseType
            ) { index ->
                selectedPurchaseType = purchaseTypeOptions[index]
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample Receipt List
            val receipts = listOf(
                Receipt("Shell", "Visa Dividend", "Gas", "2024-03-30", "$50.00"),
                Receipt("Best Buy", "MasterCard", "Electronics", "2024-04-02", "$300.00")
            )


            LazyColumn {
                items(receipts.size) { index ->
                    ReceiptBox(receipts[index])
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
        }
    }
}



@Composable
fun ReceiptBox(receipt: Receipt) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Merchant: ${receipt.merchant}",
                fontSize = 16.sp
            )
            Text(
                text = "Card Used: ${receipt.cardUsed}",
                fontSize = 16.sp
            )
            Text(
                text = "Purchase Type: ${receipt.purchaseType}",
                fontSize = 16.sp
            )
            Text(
                text = "Date: ${receipt.date}",
                fontSize = 16.sp
            )
            Text(
                text = "Total Amount: ${receipt.amount}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(2f))
                Button(
                    onClick = {  /*TODO: Handle view click*/  },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White,
                        contentColor = Color.Black),
                    modifier = Modifier.weight(3f),
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "View")
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = { /* TODO: Handle delete click */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White,
                        contentColor = Color.Black),
                    modifier = Modifier.weight(3f),
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}

data class Receipt(
    val merchant: String,
    val cardUsed: String,
    val purchaseType: String,
    val date: String,
    val amount: String
)

@Composable
@Preview
fun TempMyReceiptsScreenPreview() {
    MyReceiptsScreen()
}

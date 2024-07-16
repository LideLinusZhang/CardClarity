package edu.card.clarity.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

data class ReceiptsInfo(
    val merchant: String,
    val date: String,
    val totalAmount: String
)

@Composable
fun MyReceiptsScreen(cardName: String) {
    val receipts = listOf(
        ReceiptsInfo("Shell", "2024-05-01", "$50.00"),
        ReceiptsInfo("Walmart", "2024-06-13", "$30.00"),
        ReceiptsInfo("Starbucks", "2024-07-03", "$15.00"),
    )

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Receipts for " + cardName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn {
                items(receipts.size) { index ->
                    ReceiptsItem(
                        merchant = receipts[index].merchant,
                        date = receipts[index].date,
                        totalAmount = receipts[index].totalAmount
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ReceiptsItem(
    merchant: String,
    date: String,
    totalAmount: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        backgroundColor = Color.LightGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Merchant: $merchant",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Date: $date",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Total Amount: $totalAmount",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(80.dp))
                Button(
                    onClick = { /* TODO: Handle view click */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(text = "View")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { /* TODO: Handle delete click */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.width(60.dp))
            }
        }
    }
}

@Composable
@Preview
fun MyReceiptsScreenPreview() {
    MyReceiptsScreen("CIBC Dividend")
}
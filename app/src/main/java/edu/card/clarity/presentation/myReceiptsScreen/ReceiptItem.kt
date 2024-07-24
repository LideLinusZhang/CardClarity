package edu.card.clarity.presentation.myReceiptsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

@Composable
fun ReceiptsItem(
    receipt: ReceiptUiState,
    onRemoveReceipt: (receiptId: UUID) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Merchant: ${receipt.merchant}",
                fontSize = 16.sp
            )
            Text(
                text = "Card Used: ${receipt.creditCardName}",
                fontSize = 16.sp
            )
            Text(
                text = "Purchase Type: ${receipt.purchaseType}",
                fontSize = 16.sp
            )
            Text(
                text = "Date: ${receipt.purchaseTime}",
                fontSize = 16.sp
            )
            Text(
                text = "Total Amount: ${receipt.total}",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(2f))
                Button(
                    onClick = {  /*TODO: Handle view click*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.weight(3f),
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "View")
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    onClick = { onRemoveReceipt(receipt.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
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
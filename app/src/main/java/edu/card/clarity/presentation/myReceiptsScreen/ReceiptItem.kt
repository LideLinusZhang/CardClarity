package edu.card.clarity.presentation.myReceiptsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.presentation.common.ImageDialog
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.LightPurple
import edu.card.clarity.ui.theme.Purple40
import java.util.UUID

@Composable
fun ReceiptsItem(
    receipt: ReceiptUiState,
    onRemoveReceipt: (receiptId: UUID) -> Unit
) {
    var showImage by remember { mutableStateOf(false) }

    if (showImage && receipt.receiptImagePath != null) {
        ImageDialog(
            photoPath = receipt.receiptImagePath,
            onClose = { showImage = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(8.dp)
            .border(BorderStroke(2.dp, Purple40), shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Receipt Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LightPurple)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = receipt.merchant,
                        style = CardClarityTypography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = receipt.purchaseTime,
                        style = CardClarityTypography.bodySmall,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Card Used: ${receipt.creditCardName}",
                    style = CardClarityTypography.bodySmall,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Purchase Type: ${receipt.purchaseType}",
                    style = CardClarityTypography.bodySmall,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total Amount: $${receipt.total}",
                    style = CardClarityTypography.bodySmall,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Button(
                    onClick = { showImage = !showImage },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightPurple,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(25),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(text = "View")
                }
                Button(
                    onClick = { onRemoveReceipt(receipt.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(25),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Delete")
                }
            }
        }
    }
}


@Composable
@Preview
fun ReceiptsItemPreview() {
    val receipt = ReceiptUiState(
        id = UUID.randomUUID(),
        merchant = "costco",
        creditCardName = "AMEX Cobalt Card",
        creditCardId = UUID.randomUUID(),
        purchaseType = "Groceries",
        purchaseTime = "2024-07-24 21:07:08",
        total = "200",
        receiptImagePath = null
    )
    ReceiptsItem(receipt, onRemoveReceipt = {})
}
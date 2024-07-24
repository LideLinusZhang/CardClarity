package edu.card.clarity.presentation.myCardScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.ui.theme.CardClarityTypography
import java.util.UUID

@Composable
fun CreditCardItem(
    cardId: UUID,
    cardName: String,
    creditCardRewardTypeOrdinal: Int,
    dueDate: String,
    backgroundColor: Color,
    isReminderEnabled: Boolean,
    onBenefitButtonClick: (creditCardId: UUID, creditCardName: String, creditCardRewardTypeOrdinal: Int) -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
                    .background(Color.LightGray)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = cardName,
                        style = CardClarityTypography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    IconButton(
                        onClick = onDeleteButtonClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Due Date: $dueDate",
                    fontWeight = FontWeight.Normal,
                    style = CardClarityTypography.titleLarge,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isReminderEnabled) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Check Circle",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Cross",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        style = CardClarityTypography.titleLarge,
                        fontSize = 16.sp,
                        text = "Payment Reminder",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        onBenefitButtonClick(cardId, cardName, creditCardRewardTypeOrdinal)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.5.dp, Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "My Benefits")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardItemPreview() {
    CreditCardItem(
        cardId = UUID.randomUUID(),
        cardName = "My Credit Card",
        creditCardRewardTypeOrdinal = 1,
        dueDate = "2024-08-01",
        backgroundColor = Color(0xFFFF9EB8),
        isReminderEnabled = true,
        onBenefitButtonClick = { _, _, _ -> },
        onDeleteButtonClick = {}
    )
}

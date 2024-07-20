package edu.card.clarity.presentation.myCardScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .height(190.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.50f)
                    .fillMaxHeight()
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = cardName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = {
                            onBenefitButtonClick(cardId, cardName, creditCardRewardTypeOrdinal)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.5.dp, Color.Black),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 4.dp)
                    ) {
                        Text(text = "My Benefits")
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .background(color = Color(0xFFBFC0C2))
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp)
                ) {
                    IconButton(
                        onClick = onDeleteButtonClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(24.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(70.dp))
                    Row {
                        if (isReminderEnabled) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Check Circle",
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 1.dp)
                                    .offset(y = 3.dp),
                            )
                        } else {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Cross",
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 1.dp)
                                    .offset(y = 3.dp),
                            )
                        }
                        Text(
                            text = "Payment Reminder",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Due Date: $dueDate")
                }
            }
        }
    }
}
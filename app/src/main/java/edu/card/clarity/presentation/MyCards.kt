package edu.card.clarity.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

data class CardInfo(
    val cardName: String,
    val dueDate: String,
    val backgroundColor: Color
)

@Composable
fun MyCardsScreen() {
    val cards = listOf(
        CardInfo("TD Aeroplan Visa Infinite Card", "2024-05-24", Color(0xFFAED8FF)),
        CardInfo("American Express Platinum Card", "2024-05-13", Color(0xFFB7FF9E)),
        CardInfo("CIBC Dividend", "2024-06-03", Color(0xFFFF9EB8)),
    )

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "My Cards",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(cards.size) { index ->
                    CardItem(
                        cardName = cards[index].cardName,
                        dueDate = cards[index].dueDate,
                        backgroundColor = cards[index].backgroundColor
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CardItem(
    cardName: String,
    dueDate: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        onClick = { /* TODO: Handle receipts click */ },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        border = BorderStroke(1.5.dp, Color.Black),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 4.dp)
                    ) {
                        Text(text = "Receipts")
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .background(color = Color(0xFFBFC0C2))
                    .padding(end = 12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(100.dp))
                    Row {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Check Circle",
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 1.dp),
                        )
                        Text(
                            text = "Payment Reminder",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Due Date: $dueDate",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun MyCardsScreenPreview() {
    MyCardsScreen()
}
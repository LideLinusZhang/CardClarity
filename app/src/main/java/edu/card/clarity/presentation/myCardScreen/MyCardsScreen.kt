package edu.card.clarity.presentation.myCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

data class CardInfo(
    val cardName: String,
    val dueDate: String,
    val backgroundColor: Color
)

@Composable
fun MyCardsScreen(viewModel: MyCardsScreenViewModel = hiltViewModel()) {
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
                    CreditCardItem(
                        cardName = cards[index].cardName,
                        dueDate = cards[index].dueDate,
                        backgroundColor = cards[index].backgroundColor,
                        onReceiptButtonClick = {}
                    )
                    Spacer(modifier = Modifier.height(24.dp))
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
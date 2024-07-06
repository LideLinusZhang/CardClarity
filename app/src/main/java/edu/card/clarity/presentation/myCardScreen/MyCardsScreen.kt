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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

data class CardInfo(
    val cardName: String,
    val dueDate: String,
    val backgroundColor: Color
)

@Composable
fun MyCardsScreen(viewModel: MyCardsScreenViewModel = hiltViewModel()) {
    val creditCardItemUiStates by viewModel.uiState.collectAsStateWithLifecycle()

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
                items(creditCardItemUiStates.size) { index ->
                    CreditCardItem(
                        cardName = creditCardItemUiStates[index].cardName,
                        dueDate = creditCardItemUiStates[index].dueDate,
                        backgroundColor = creditCardItemUiStates[index].backgroundColor,
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
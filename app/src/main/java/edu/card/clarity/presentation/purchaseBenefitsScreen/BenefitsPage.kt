
package edu.card.clarity.presentation.purchaseBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.presentation.purchaseBenefitsScreen.BenefitsPageViewModel.CreditCardItemUiState

@Composable
fun BenefitsScreen(navController: NavController, category: String, viewModel: BenefitsPageViewModel = hiltViewModel()) {
    val creditCards by viewModel.creditCards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
    ) {
        Text(
            text = "Benefits for $category",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(creditCards) { card ->
                CreditCardItem(card)
            }
        }
    }
}

@Composable
fun CreditCardItem(card: CreditCardItemUiState) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Card: ${card.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        card.rewards.forEach { reward ->
            Text(
                text = "Benefit: ${reward.purchaseType} - ${reward.percentage}%",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
@Preview
fun BenefitsScreenPreview() {
    val navController = rememberNavController()
    BenefitsScreen(navController, category = "Pharmacy")
}
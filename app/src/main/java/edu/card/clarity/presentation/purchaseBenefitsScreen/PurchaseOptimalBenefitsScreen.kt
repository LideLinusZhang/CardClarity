package edu.card.clarity.presentation.purchaseBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.enums.PurchaseType

@Composable
fun PurchaseOptimalBenefitsScreen(
    navController: NavController,
    category: PurchaseType,
    viewModel: PurchaseOptimalBenefitsScreenViewModel = hiltViewModel()
) {
    val creditCards by viewModel.creditCards.collectAsState()
    val optimalCreditCard by viewModel.optimalCreditCard.collectAsState()
    val optimalCardMessage by viewModel.optimalCardMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Best Card for ${category.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (optimalCreditCard != null) {
            OptimalCreditCardItem(optimalCreditCard!!, category)
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = optimalCardMessage ?: "",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Text(
            text = "Other Available Benefits for ${category.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(creditCards) { card ->
                CreditCardItem(card)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Navigate to record receipt screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Record a Receipt")
                }
            }
        }
    }
}

@Composable
fun OptimalCreditCardItem(card: PurchaseOptimalBenefitsScreenViewModel.CreditCardItemUiState, category: PurchaseType) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Text(
                text = card.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            card.rewards.forEach { reward ->
                Text(
                    text = "${reward.purchaseType} - ${reward.description}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun CreditCardItem(card: PurchaseOptimalBenefitsScreenViewModel.CreditCardItemUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(8.dp, shape = MaterialTheme.shapes.medium),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = card.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            card.rewards.forEach { reward ->
                Text(
                    text = "${reward.purchaseType} - ${reward.description}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
@Preview
fun BenefitsScreenPreview() {
    val navController = rememberNavController()
    PurchaseOptimalBenefitsScreen(navController, category = PurchaseType.Pharmacy)
}

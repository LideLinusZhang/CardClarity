package edu.card.clarity.presentation.purchaseBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.CustomButton
import edu.card.clarity.presentation.common.InfoBoxItem
import edu.card.clarity.presentation.utils.Destinations
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue

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
        Spacer(modifier = Modifier.height(16.dp))
        OptimalBenefitTitle(category.toString())
        Spacer(modifier = Modifier.height(16.dp))
        if (optimalCreditCard != null) {
            val rewardsDescription = optimalCreditCard!!.rewards.joinToString(separator = "\n") { "${it.purchaseType} - ${it.description}" }
            InfoBoxItem(mainTitle = optimalCreditCard!!.name, subtitle = rewardsDescription)
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
            style = CardClarityTypography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(creditCards) { card ->
                val rewardsDescription = card.rewards.joinToString(separator = "\n") { "${it.purchaseType} - ${it.description}" }
                InfoBoxItem(mainTitle = card.name, subtitle = rewardsDescription)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    text = "Record a Receipt",
                    onClick = {
                        navController.navigate(Destinations.RECORD_RECEIPT)
                    }
                )
            }
        }
    }
}

@Composable
fun OptimalBenefitTitle(purchaseTypeString: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = DarkAccentBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Your Optimal Card Suggestion for",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = CardClarityTypography.titleLarge,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = purchaseTypeString,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            style = CardClarityTypography.titleLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    }
}

@Composable
@Preview
fun BenefitsScreenPreview() {
    val navController = rememberNavController()
    PurchaseOptimalBenefitsScreen(navController, category = PurchaseType.Pharmacy)
}
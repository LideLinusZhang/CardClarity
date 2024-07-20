package edu.card.clarity.presentation.myBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.common.ChipFilter
import edu.card.clarity.presentation.utils.Destinations
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import kotlinx.coroutines.launch

@Composable
fun MyBenefitsScreen(
    creditCardName: String,
    navController: NavHostController,
    viewModel: MyBenefitsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val purchaseTypes = PurchaseType.entries.map { it.name }

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "My Benefits for $creditCardName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChipFilter(
                filterOptions = purchaseTypes,
                initiallySelectedOptionIndices = listOf(0),
                onSelectedChanged = { _, _ -> /* Handle filter change */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                contentPadding = PaddingValues(bottom = 45.dp)
            ) {
                items(uiState) { item ->
                    BenefitItem(
                        purchaseType = item.purchaseType,
                        benefit = item.rewardDescription
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    scope.launch {
                        navController.navigate("${Destinations.ADD_BENEFIT}/${viewModel.cardIdString}/$creditCardName/${viewModel.cardRewardTypeOrdinal}")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Discovered a new benefit for this card? Add it here!")
            }
        }
    }
}

@Composable
fun BenefitItem(purchaseType: String, benefit: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Purchase Type: $purchaseType", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Benefit: $benefit", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
@Preview
fun MyBenefitsScreenPreview() {
    MyBenefitsScreen(
        creditCardName = "AMEX Cobalt Card",
        navController = rememberNavController()
    )
}

package edu.card.clarity.presentation.myBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.ChipFilter
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.util.UUID

@Composable
fun MyBenefitsScreen(cardName: String, cardId: UUID?, viewModel: MyBenefitsScreenViewModel = hiltViewModel()) {
    val benefitItemUiStates by viewModel.benefitItems.collectAsState()

    LaunchedEffect(cardId) {
        viewModel.loadBenefits(cardId)
    }

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
                text = "My Benefits for $cardName",
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
                items(benefitItemUiStates.size) { index ->
                    val item = benefitItemUiStates[index]
                    BenefitItem(
                        purchaseType = item.purchaseType,
                        benefit = item.benefit
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { /* Handle add new benefit action */ },
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
    MyBenefitsScreen(cardName = "CIBC Dividend", cardId = null)
}

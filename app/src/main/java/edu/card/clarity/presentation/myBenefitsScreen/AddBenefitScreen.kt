package edu.card.clarity.presentation.myBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.util.UUID

@Composable
fun AddBenefitScreen(cardName: String, rewardType: String, cardId: UUID?, viewModel: MyBenefitsScreenViewModel = hiltViewModel()) {
    var selectedPurchaseTypeIndex by remember { mutableIntStateOf(0) }
    var percentage by remember { mutableStateOf("") }
    var multiplier by remember { mutableStateOf("") }
    var isValidPercentage by remember { mutableStateOf(true) }
    var isValidMultiplier by remember { mutableStateOf(true) }

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Add New Benefit For:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = cardName.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                        color = Color(0xFF1A73E8),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = rewardType,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                        color = Color(0xFF34A853),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Dropdown for Purchase Types
            DropdownMenu(
                label = "Purchase Type",
                options = PurchaseType.entries.map { it.name },
                selectedOption = PurchaseType.entries[selectedPurchaseTypeIndex].name,
                onOptionSelected = { index -> selectedPurchaseTypeIndex = index }
            )

            // Conditionally display input fields based on RewardType:
            // Cashback: percentage.
            // PointsBack: multiplier and points system.
            if (rewardType.equals("Cashback", ignoreCase = true)) {
                OutlinedTextField(
                    value = percentage,
                    onValueChange = {
                        percentage = it
                        isValidPercentage = percentage.toFloatOrNull()?.let { value -> value in 0.0..100.0 } ?: false
                    },
                    label = { Text("Percentage (%)") },
                    isError = !isValidPercentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                if (!isValidPercentage) {
                    Text(
                        text = "Please enter a valid percentage between 0 and 100",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else if (rewardType.equals("PointBack", ignoreCase = true)) {
                OutlinedTextField(
                    value = multiplier,
                    onValueChange = {
                        multiplier = it
                        isValidMultiplier = multiplier.toFloatOrNull()?.let { value -> value >= 1.0 } ?: false
                    },
                    label = { Text("Multiplier") },
                    isError = !isValidMultiplier,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                if (!isValidMultiplier) {
                    Text(
                        text = "Please enter a valid multiplier (>= 1.0)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Button(
                onClick = {
                    val rewardValue = if (rewardType.equals("Cashback", ignoreCase = true)) {
                        (percentage.toFloatOrNull() ?: 0f) / 100f
                    } else {
                        multiplier.toFloatOrNull() ?: 0f
                    }
                    viewModel.addBenefit(cardId, PurchaseType.entries[selectedPurchaseTypeIndex], rewardValue)
                },
                modifier = Modifier.padding(top = 16.dp),
                enabled = (rewardType.equals("Cashback", ignoreCase = true) && isValidPercentage) ||
                        (rewardType.equals("PointBack", ignoreCase = true) && isValidMultiplier)
            ) {
                Text(text = "Add Benefit")
            }
        }
    }
}

@Composable
@Preview
fun AddBenefitScreenPreview() {
    AddBenefitScreen(cardName = "CIBC Dividend", rewardType = "Cashback", cardId = null)
}

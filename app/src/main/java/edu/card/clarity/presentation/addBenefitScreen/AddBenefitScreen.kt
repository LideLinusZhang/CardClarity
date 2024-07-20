package edu.card.clarity.presentation.addBenefitScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import edu.card.clarity.enums.RewardType
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.utils.displayString
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography

@Composable
fun AddBenefitScreen(
    creditCardName: String,
    creditCardRewardType: RewardType,
    viewModel: AddBenefitScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                        text = creditCardName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                        color = Color(0xFF1A73E8),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = creditCardRewardType.displayString,
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
                options = viewModel.purchaseTypeOptionStrings,
                selectedOption = uiState.selectedPurchaseType,
                onOptionSelected = viewModel::updateSelectedPurchaseType
            )

            // Conditionally display input fields based on RewardType:
            // Cashback: percentage.
            // PointsBack: multiplier and points system.
            OutlinedTextField(
                value = uiState.factor,
                onValueChange = viewModel::updateFactor,
                label = {
                    Text(
                        text = when (creditCardRewardType) {
                            RewardType.CashBack -> "Percentage (%)"
                            RewardType.PointBack -> "Multiplier"
                        }
                    )
                },
                isError = !uiState.isFactorValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            if (!uiState.isFactorValid) {
                Text(
                    text = when (creditCardRewardType) {
                        RewardType.CashBack -> "Please enter a valid percentage between 0 and 100"
                        RewardType.PointBack -> "Please enter a valid multiplier (>= 1.0)"
                    },
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = viewModel::addBenefit,
                modifier = Modifier.padding(top = 16.dp),
                enabled = uiState.isFactorValid
            ) {
                Text(text = "Add Benefit")
            }
        }
    }
}

@Composable
@Preview
fun AddBenefitScreenPreview() {
    AddBenefitScreen("AMEX Cobalt Card", RewardType.PointBack)
}

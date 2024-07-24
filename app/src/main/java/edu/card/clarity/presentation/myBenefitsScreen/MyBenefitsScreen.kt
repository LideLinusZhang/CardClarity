package edu.card.clarity.presentation.myBenefitsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.common.CustomButton
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.presentation.common.InfoBoxItem
import edu.card.clarity.presentation.utils.Destinations
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue

@Composable
fun MyBenefitsScreen(
    creditCardName: String,
    navController: NavHostController,
    viewModel: MyBenefitsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val purchaseTypes = listOf("ALL") + PurchaseType.entries.map { it.name }
    var selectedFilter by remember { mutableStateOf(purchaseTypes[0]) }

    CardClarityTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                MyBenefitsTitle(creditCardName)
                Spacer(modifier = Modifier.height(16.dp))
                DropdownMenu(
                    label = "Filter by Your Purchase Type",
                    options = purchaseTypes,
                    selectedOption = selectedFilter,
                    onOptionSelected = { index ->
                        selectedFilter = purchaseTypes[index]
                        viewModel.updateFilter(selectedFilter)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(uiState) { item ->
                InfoBoxItem(mainTitle = item.purchaseType, subtitle = item.rewardDescription)
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.padding(bottom = 52.dp)) {
                    CustomButton("Discovered a new benefit for this card? Add it here!") {
                        navController.navigate("${Destinations.ADD_BENEFIT}/${viewModel.cardIdString}/$creditCardName/${viewModel.cardRewardTypeOrdinal}")
                    }
                }
            }
        }
    }
}

@Composable
fun MyBenefitsTitle(creditCardName: String) {
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
                text = "My Benefits for",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = CardClarityTypography.titleLarge,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = creditCardName,
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
fun MyBenefitsScreenPreview() {
    MyBenefitsScreen(
        creditCardName = "AMEX Cobalt Card",
        navController = rememberNavController()
    )
}

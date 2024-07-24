package edu.card.clarity.presentation.myCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.presentation.common.ChipFilter
import edu.card.clarity.presentation.utils.Destinations
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue

data class CardInfo(
    val cardName: String,
    val dueDate: String,
    val backgroundColor: Color
)

@Composable
fun MyCardsScreen(navController: NavHostController, viewModel: MyCardsScreenViewModel = hiltViewModel()) {
    val creditCardItemUiStates by viewModel.uiState.collectAsStateWithLifecycle()

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Star, contentDescription = "Credit Card Icon", tint = DarkAccentBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "My Wallet",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = CardClarityTypography.titleLarge,
                    color = Color.Black
                )
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            ChipFilter(
                filterOptions = viewModel.rewardTypeFilterOptionStrings,
                initiallySelectedOptionIndices = viewModel.rewardTypeFilterInitiallySelectedOptionIndices,
                onSelectedChanged = viewModel::updateRewardTypeFilter
            )
            ChipFilter(
                filterOptions = viewModel.cardNetworkTypeFilterOptionStrings,
                initiallySelectedOptionIndices = viewModel.cardNetworkTypeFilterInitiallySelectedOptionIndices,
                onSelectedChanged = viewModel::updateCardNetworkFilter
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn (
                contentPadding = PaddingValues(bottom = 45.dp)
            ){
                items(creditCardItemUiStates.size) { index ->
                    val item = creditCardItemUiStates[index]
                    CreditCardItem(
                        cardId = item.id,
                        cardName = item.cardName,
                        creditCardRewardTypeOrdinal = item.rewardTypeOrdinal,
                        dueDate = item.dueDate,
                        backgroundColor = item.backgroundColor,
                        isReminderEnabled = item.isReminderEnabled,
                        onBenefitButtonClick = { cardId, cardName, cardRewardType ->
                            navController.navigate("${Destinations.MY_BENEFITS}/$cardId/$cardName/$cardRewardType")
                        },
                        onDeleteButtonClick = {
                            viewModel.deleteCreditCard(
                                creditCardItemUiStates[index].id
                            )
                        },
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
    MyCardsScreen(navController = rememberNavController())
}
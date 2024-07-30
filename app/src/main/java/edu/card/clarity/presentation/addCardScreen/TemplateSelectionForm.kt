package edu.card.clarity.presentation.addCardScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.card.clarity.presentation.common.CustomButton
import edu.card.clarity.presentation.common.DatePickerField
import edu.card.clarity.presentation.common.DropdownMenu
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.LightPurple
import edu.card.clarity.ui.theme.Purple40
import java.util.Calendar

@Composable
fun UseTemplateCardInformationForm(
    navController: NavController,
    viewModel: TemplateSelectionFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val templateOptions by viewModel.templateOptionStrings.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val statementDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.updateMostRecentStatementDate(
                    year,
                    month,
                    dayOfMonth
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val paymentDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.updateMostRecentPaymentDueDate(
                    year,
                    month,
                    dayOfMonth
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenu(
            label = "Select Credit Card Template",
            options = templateOptions,
            selectedOption = uiState.selectedTemplateName,
            onOptionSelected = viewModel::updateTemplateSelection
        )

        if (uiState.showCardInfo) {
            TemplateCardInfoBox(
                cardName = uiState.cardName,
                cardNetwork = uiState.cardNetworkType,
                rewardType = uiState.rewardType
            )
            DatePickerField(
                date = uiState.mostRecentStatementDate,
                label = "Most Recent Statement Date",
                onClick = statementDatePickerDialog::show
            )
            DatePickerField(
                date = uiState.mostRecentPaymentDueDate,
                label = "Most Recent Payment Due Date",
                onClick = paymentDatePickerDialog::show
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payment Reminder:",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Switch(
                    checked = uiState.isReminderEnabled,
                    onCheckedChange = viewModel::updateReminderEnabled
                )
            }
            CustomButton(
                text = "Add Card to My Cards",
                onClick = {
                    viewModel.createCreditCard()
                    navController.navigate("myCards")
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Non-Editable Card information based on the template chosen
@Composable
fun TemplateCardInfoBox(
    cardName: String,
    cardNetwork: String,
    rewardType: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = LightPurple,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp)
                    .background(color = Purple40)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Card Name: $cardName",
                    style = CardClarityTypography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Card Network: $cardNetwork",
                    style = CardClarityTypography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Reward Type: $rewardType",
                    style = CardClarityTypography.titleMedium,
                    color = Color.Black
                )
            }
        }
    }
}

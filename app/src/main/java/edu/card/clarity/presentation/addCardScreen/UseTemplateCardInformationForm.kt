package edu.card.clarity.presentation.addCardScreen

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.presentation.common.DatePickerField
import java.util.Calendar

@Composable
fun UseTemplateCardInformationForm(template: CreditCardInfo) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var statementDate by remember { mutableStateOf("") }
    var paymentDueDate by remember { mutableStateOf("") }
    var isReminderEnabled by remember { mutableStateOf(true) }

    val statementDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                statementDate = "$dayOfMonth/${month + 1}/$year"
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
                paymentDueDate = "$dayOfMonth/${month + 1}/$year"
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
        // Non-Editable Card information based on the template chosen
        Text(
            text = "Card Name: ${template.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Card Network: ${template.cardNetworkType}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Reward Type: ${template.rewardType}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        DatePickerField(
            date = statementDate,
            label = "Most Recent Statement Date",
            onClick = statementDatePickerDialog::show
        )
        DatePickerField(
            date = paymentDueDate,
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
                checked = isReminderEnabled,
                onCheckedChange = { isReminderEnabled = it }
            )
        }

        Button(
            onClick = {
                // ADD CARD ACTION
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            Text(text = "Add Card to My Cards")
        }
    }
}

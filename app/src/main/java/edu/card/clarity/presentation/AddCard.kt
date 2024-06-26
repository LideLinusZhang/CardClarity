package edu.card.clarity.presentation

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import java.util.*

@Composable
fun AddCardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Add a Card",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Text(
            text = "Card Information Form",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CardInformationForm(navController)
    }
}

@Composable
fun CardInformationForm(navController: NavController) {
    val cardTypes = listOf("Credit Card", "Debit Card", "Prepaid Card")
    val cardNames = listOf("Visa", "MasterCard", "American Express")
    val cardIssuers = listOf("RBC", "TD", "Scotiabank", "BMO", "CIBC")

    var selectedCardType by remember { mutableStateOf(cardTypes[0]) }
    var selectedCardName by remember { mutableStateOf(cardNames[0]) }
    var selectedCardIssuer by remember { mutableStateOf(cardIssuers[0]) }
    var statementDate by remember { mutableStateOf("") }
    var paymentDate by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            statementDate = "$year-${month + 1}-$dayOfMonth"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    val paymentDatePickerDialog = remember {
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            paymentDate = "$year-${month + 1}-$dayOfMonth"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenu(label = "Card Type", options = cardTypes, selectedOption = selectedCardType) {
            selectedCardType = it
        }
        DropdownMenu(label = "Card Name", options = cardNames, selectedOption = selectedCardName) {
            selectedCardName = it
        }
        DropdownMenu(label = "Card Issuer", options = cardIssuers, selectedOption = selectedCardIssuer) {
            selectedCardIssuer = it
        }
        DatePickerField(value = statementDate, label = "Next Upcoming Statement Date", onClick = {
            datePickerDialog.show()
        })
        DatePickerField(value = paymentDate, label = "Next Upcoming Payment Date", onClick = {
            paymentDatePickerDialog.show()
        })

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
                checked = reminder,
                onCheckedChange = { reminder = it }
            )
        }

        Button(
            onClick = {
                navController.navigate("myCards")
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

@Composable
fun DropdownMenu(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedOption)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerField(value: String, label: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Text(text = value.ifEmpty { "Select Date" }, color = Color.Black)
        }
    }
}

@Composable
@Preview
fun AddCardScreenPreview() {
    val navController = rememberNavController()
    AddCardScreen(navController)
}

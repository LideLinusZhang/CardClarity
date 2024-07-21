package edu.card.clarity.presentation.addCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.presentation.common.DropdownMenu

@Composable
fun AddCardScreen(navController: NavController, viewModel: AddCardViewModel = hiltViewModel()) {
    val templates by viewModel.templates.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedTemplateIndex by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
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

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Use a Template") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Add Custom Card") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTabIndex == 0) {
            Column {
                DropdownMenu(
                    label = "Credit Card",
                    options = templates.map { it.name },
                    selectedOption = if (selectedTemplateIndex != -1) templates[selectedTemplateIndex].name else "Select a template",
                    onOptionSelected = { selectedTemplateIndex = it }
                )
                if (selectedTemplateIndex != -1) {
                    val template = templates[selectedTemplateIndex]
                    UseTemplateCardInformationForm(template)
                }
            }
        } else {
            AddCustomCardContent(navController)
        }
    }
}

@Composable
fun AddCustomCardContent(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CardInformationForm(navController)
    }
}

@Composable
@Preview
fun AddCardScreenPreview() {
    val navController = rememberNavController()
    AddCardScreen(navController)
}

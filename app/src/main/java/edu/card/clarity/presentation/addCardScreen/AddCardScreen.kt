package edu.card.clarity.presentation.addCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddCardScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(AddCardTabOption.Template) }

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
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == AddCardTabOption.Template,
                onClick = { selectedTab = AddCardTabOption.Template },
                text = { Text("Use a Template") }
            )
            Tab(
                selected = selectedTab == AddCardTabOption.Customize,
                onClick = { selectedTab = AddCardTabOption.Customize },
                text = { Text("Add Custom Card") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            AddCardTabOption.Template -> UseTemplateCardInformationForm(navController)
            AddCardTabOption.Customize -> AddCustomCardContent(navController)
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

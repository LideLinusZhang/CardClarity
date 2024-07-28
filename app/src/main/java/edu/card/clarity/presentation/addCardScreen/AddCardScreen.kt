package edu.card.clarity.presentation.addCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.ui.theme.CardClarityTypography
import edu.card.clarity.ui.theme.DarkAccentBlue

@Composable
fun AddCardScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(AddCardTabOption.Template) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Icon", tint = DarkAccentBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Card to My Wallet",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    style = CardClarityTypography.titleLarge,
                    color = Color.Black
                )
            }
        }
        item { HorizontalDivider(thickness = 1.dp, color = Color.Gray) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Text(
                text = "Card Information Form",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
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
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            when (selectedTab) {
                AddCardTabOption.Template -> UseTemplateCardInformationForm(navController)
                AddCardTabOption.Customize -> AddCustomCardContent(navController)
            }
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

package edu.card.clarity.presentation.myReceiptsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MyReceiptsScreen(navController: NavController) {

    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "My Receipts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CardClarityTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ReceiptsFilter()

            Box(modifier = Modifier.weight(0.5f)) {
                Receipts(navController)
            }

            Button(
                onClick = { /* TODO: Handle record receipt action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White,
                    contentColor = Color.Black),
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(25),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Record a Receipt")
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
@Preview
fun MyReceiptsScreenPreview() {
    val navController = rememberNavController()
    MyReceiptsScreen(navController)
}
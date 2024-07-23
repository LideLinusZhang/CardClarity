package edu.card.clarity.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.card.clarity.R
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.presentation.utils.Destinations

@Composable
fun PurchaseScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Best Card for Every Purchase",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Categories:",
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategoryGrid(navController)
    }
}

@Composable
fun CategoryGrid(navController: NavController) {
    val categories = PurchaseType.entries

    Column {
        for (i in categories.indices step 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (j in i until i + 3) {
                    if (j < categories.size) {
                        CategoryCard(category = categories[j]) {
                            navController.navigate("${Destinations.PURCHASE_OPTIMAL_BENEFITS}/${categories[j].name}")
                        }
                    } else {
                        Spacer(modifier = Modifier.size(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: PurchaseType, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        ) {
            val imageRes = when (category) {
                PurchaseType.Pharmacy -> R.drawable.pharmacy
                PurchaseType.Entertainment -> R.drawable.entertainment
                PurchaseType.Furniture -> R.drawable.furniture
                PurchaseType.Gas -> R.drawable.gas
                PurchaseType.Hotel -> R.drawable.hotel
                PurchaseType.HomeImprovement -> R.drawable.home_improvement
                PurchaseType.Groceries -> R.drawable.groceries
                PurchaseType.Restaurants -> R.drawable.restaurants
                PurchaseType.Travel -> R.drawable.travel
                PurchaseType.Others -> R.drawable.others
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = category.name,
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
@Preview
fun PurchaseScreenPreview() {
    val navController = rememberNavController()
    PurchaseScreen(navController)
}

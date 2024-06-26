package edu.card.clarity.presentation

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        RewardsSummary(userName = "Sophie Chan")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            CardBox(label = "My Cards") {
                navController.navigate("myCards")
            }
            CardBox(label = "Upcoming Payments") {
                navController.navigate("upcomingPayments")
            }
        }

    }
}

@Composable
fun RewardsSummary(userName: String) {
    val months = listOf("March", "April", "May")
    val data = listOf(100, 170, 300)
    val maxData = data.maxOrNull() ?: 1


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 12.dp),
            text = "Hi $userName,",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )
        Text(
            text = "Your 3 month rewards summary.",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 250.dp, max = 324.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            months.zip(data).forEach { (month, value) ->
                Bar(month = month, value = value, maxValue = maxData)
            }
        }
    }
}

@Composable
fun Bar(month: String, value: Int, maxValue: Int) {
    val barHeightFraction = value.toFloat() / maxValue.toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 90.dp, max = 100.dp)
                .fillMaxHeight(fraction = barHeightFraction)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "+$$value",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = month,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CardBox(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .heightIn(min = 80.dp, max = 80.dp)
        ) {
            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
            )
        }
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}

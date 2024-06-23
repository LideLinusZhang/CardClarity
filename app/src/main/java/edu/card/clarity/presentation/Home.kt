package edu.card.clarity.presentation

import androidx.compose.foundation.background
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

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Header(userName = "John Doe")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BarBox(label = "March")
            BarBox(label = "April")
            BarBox(label = "May")
        }
        Column( modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            CardBox(label = "My Cards")
            CardBox(label = "Upcoming Payments")
        }

    }
}

@Composable
fun Header(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hi $userName",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black
        )
        Text(
            text = "Your 3 month rewards summary",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun BarGroup(savingsOne: Number,
             monthOne: String,
             savingsTwo: Number,
             monthTwo: String,
             savingsThree: Number,
             monthThree: String) {

}

@Composable
fun Bar(label: String, savings: Number) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun BarBox(label: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun CardBox(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}

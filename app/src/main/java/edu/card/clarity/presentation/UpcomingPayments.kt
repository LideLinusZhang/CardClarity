package edu.card.clarity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.time.LocalDate
import java.time.YearMonth


val cards = listOf(
    CardInfo(cardName = "TD Aeroplan Visa Infinite Card", dueDate = "2024-06-29", backgroundColor = Color(0xFFAED8FF)),
    CardInfo(cardName = "American Express Platinum Card", dueDate = "2024-06-13", backgroundColor = Color(0xFFB7FF9E)),
    CardInfo(cardName = "CIBC Dividend", dueDate = "2024-06-03", backgroundColor = Color(0xFFFF9EB8)),
)

@Composable
fun UpcomingPaymentsScreen() {
    CardClarityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 32.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header()
            CalendarPager(cards)
            UpcomingPayment(cards)
        }
    }
}

@Composable
fun Header() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        text = "Upcoming Credit Card Payments",
        style = CardClarityTypography.titleLarge,
        color = Color.Black
    )
}

@Composable
fun CalendarPager(cards: List<CardInfo>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column {
        MonthView(
            month = currentMonth,
            cards = cards,
            onMonthChange = { newMonth -> currentMonth = newMonth }
        )
    }
}

@Composable
fun MonthView(
    month: YearMonth,
    cards: List<CardInfo>,
    onMonthChange: (YearMonth) -> Unit
) {
    val daysInMonth: Int = month.lengthOfMonth()
    val firstOfMonth: LocalDate = month.atDay(1)
    val dayOfWeekOffset: Int = firstOfMonth.dayOfWeek.value % 7
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column {
        // month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // prev
            IconButton(onClick = { onMonthChange(month.minusMonths(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
            }

            // month + year
            Text(
                text = "${month.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${month.year}",
                style = CardClarityTypography.titleLarge
            )

            // next
            IconButton(onClick = { onMonthChange(month.plusMonths(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }

        // calendar
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // days of the week header
            items(days.size) { index ->
                Text(
                    text = days[index],
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black,
                    style = CardClarityTypography.bodyLarge.copy(textAlign = TextAlign.Center),
                )
            }

            items(daysInMonth + dayOfWeekOffset) { index: Int ->
                if (index >= dayOfWeekOffset) {
                    val day: Int = index - dayOfWeekOffset + 1
                    DayCell(day = day, month = month, cards = cards)
                } else {
                    Box(Modifier.size(40.dp))
                }
            }
        }
    }
    PaymentLegend(cards = cards)
}

@Composable
fun DayCell(day: Int, month: YearMonth, cards: List<CardInfo>) {
    val date: LocalDate = month.atDay(day)
    // check if any card has a due date with the same day of the month as 'day'
    val paymentColor = cards.find { card ->
        val cardDueDate = LocalDate.parse(card.dueDate)
        cardDueDate.dayOfMonth == day
    }?.backgroundColor ?: Color.LightGray

    Box(
        modifier = Modifier
            .background(paymentColor, RoundedCornerShape(4.dp))
            .padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.toString(),
            style = CardClarityTypography.bodyLarge.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PaymentLegend(cards: List<CardInfo>) {
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(4))
    ) {
        cards.forEach { card ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .background(color = card.backgroundColor, shape = RoundedCornerShape(20))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = card.cardName,
                    style = CardClarityTypography.bodyLarge
                )
            }
        }
    }
}



@Composable
fun UpcomingPayment(cards: List<CardInfo>) {
    val sortedPayments = cards.sortedBy { LocalDate.parse(it.dueDate) }
    val nextPayment = sortedPayments.firstOrNull { LocalDate.parse(it.dueDate).isAfter(LocalDate.now()) }

    Column (
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Next payment",
            style = CardClarityTypography.titleLarge,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        if (nextPayment != null) {
            Box(
                modifier = Modifier
                    .background(color = nextPayment.backgroundColor, shape = RoundedCornerShape(4))
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "${nextPayment.cardName}\nDue Date: ${nextPayment.dueDate}",
                        style = CardClarityTypography.bodyLarge
                    )
                }
            }
        } else {
            Text(
                text = "No upcoming payments",
                style = CardClarityTypography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUpcomingPaymentsScreen() {
    UpcomingPaymentsScreen()
}

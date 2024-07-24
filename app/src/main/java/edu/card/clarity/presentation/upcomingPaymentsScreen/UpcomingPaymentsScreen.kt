package edu.card.clarity.presentation.upcomingPaymentsScreen

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun UpcomingPaymentsScreen(viewModel: PaymentDueDateViewModel = hiltViewModel()) {
    val cards by viewModel.uiState.collectAsState()

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
            PaymentLegend(cards)
            UpcomingPayment(cards, viewModel)
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.DateRange, contentDescription = "Date Range Icon", tint = Color(0xFF6200EE))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Upcoming Credit Card Payments",
            style = CardClarityTypography.titleLarge,
            color = Color.Black
        )
    }
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
}

@Composable
fun CalendarPager(cards: List<PaymentDueDateUiState>) {
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
    cards: List<PaymentDueDateUiState>,
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

            // day cells with payment coloring
            items(daysInMonth + dayOfWeekOffset) { index: Int ->
                if (index >= dayOfWeekOffset) {
                    val day: Int = index - dayOfWeekOffset + 1
                    DayCell(day = day, cards = cards)
                } else {
                    Box(Modifier.size(40.dp))
                }
            }
        }
    }
}

@Composable
fun DayCell(day: Int, cards: List<PaymentDueDateUiState>) {
    val paymentColor = cards.find { card ->
        val cardDueDate = LocalDate.of(
            card.dueDate.get(Calendar.YEAR),
            card.dueDate.get(Calendar.MONTH) + 1,  // Calendar.MONTH is zero-based in java.util.Calendar
            card.dueDate.get(Calendar.DAY_OF_MONTH)
        )
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
fun PaymentLegend(cards: List<PaymentDueDateUiState>) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8))
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
fun UpcomingPayment(cards: List<PaymentDueDateUiState>, viewModel: PaymentDueDateViewModel) {
    val nextPaymentInfo = viewModel.getNextPayment(cards)
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Next Payment",
            style = CardClarityTypography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (cards.isEmpty()) {
            Text(
                text = "No upcoming payments",
                style = CardClarityTypography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        } else if (nextPaymentInfo != null) {
            val formattedDueDate = nextPaymentInfo.second.format(dateFormatter)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = Color(0xFFE3F2FD),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(56.dp)
                            .background(color = Color(0xFF42A5F5))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = nextPaymentInfo.first,
                            style = CardClarityTypography.titleMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Due Date: $formattedDueDate",
                            style = CardClarityTypography.bodyLarge,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No upcoming payments",
                style = CardClarityTypography.bodyLarge,
                color = Color.Gray,
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

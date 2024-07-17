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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.card.clarity.presentation.myCardScreen.CardInfo
import edu.card.clarity.ui.theme.CardClarityTheme
import edu.card.clarity.ui.theme.CardClarityTypography
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun UpcomingPaymentsScreen(viewModel: PaymentDueDateViewModel = viewModel()) {
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
            UpcomingPayment(cards)
        }
    }
}

@Composable
fun Header() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        text = "Upcoming Credit Card Payments",
        style = CardClarityTypography.titleLarge,
        color = Color.Black
    )
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

            // Day cells with payment coloring
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
}

@Composable
fun DayCell(day: Int, month: YearMonth, cards: List<PaymentDueDateUiState>) {
    val date: LocalDate = month.atDay(day)
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
fun UpcomingPayment(cards: List<PaymentDueDateUiState>) {
    val sortedPayments = cards.sortedBy { card ->
        LocalDate.of(
            card.dueDate.get(Calendar.YEAR),
            card.dueDate.get(Calendar.MONTH) + 1,  // Calendar.MONTH is zero-based
            card.dueDate.get(Calendar.DAY_OF_MONTH)
        )
    }
    val now = LocalDate.now()
    val nextPayment = sortedPayments.firstOrNull { it.dueDate.toLocalDate().isAfter(now) }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Next payment",
            style = CardClarityTypography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
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
                    val dueDate = LocalDate.of(
                        nextPayment.dueDate.get(Calendar.YEAR),
                        nextPayment.dueDate.get(Calendar.MONTH) + 1,
                        nextPayment.dueDate.get(Calendar.DAY_OF_MONTH)
                    )
                    Text(
                        text = "${nextPayment.cardName}\nDue Date: $dueDate",
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

private fun Calendar.toLocalDate(): LocalDate {
    return LocalDate.of(
        this.get(Calendar.YEAR),
        this.get(Calendar.MONTH) + 1,  // Adjust for zero-based month index
        this.get(Calendar.DAY_OF_MONTH)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewUpcomingPaymentsScreen() {
    UpcomingPaymentsScreen()
}

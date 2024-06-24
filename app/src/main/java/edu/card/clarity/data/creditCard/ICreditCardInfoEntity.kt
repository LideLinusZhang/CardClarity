package edu.card.clarity.data.creditCard

import android.icu.util.Calendar
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.CalendarConverter
import java.util.Date
import java.util.UUID

interface ICreditCardInfoEntity {
    val id: UUID
    val name: String
    val statementDate: Calendar
    val paymentDueDate: Calendar
}

package edu.card.clarity.data.creditCard.cashBack

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.card.clarity.data.converters.CalendarConverter
import edu.card.clarity.data.creditCard.ICreditCardInfoEntity
import org.jetbrains.annotations.NotNull
import java.util.Date
import java.util.UUID

@Entity(tableName = "cashBackCreditCard")
@TypeConverters(CalendarConverter::class)
data class CashBackCreditCardInfoEntity(
    @PrimaryKey override val id: UUID,
    override val name: String,
    override val paymentDueDate: Calendar,
    override val statementDate: Calendar
) : ICreditCardInfoEntity
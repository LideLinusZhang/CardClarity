package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar

open class CreditCardInfo(
    val name: String,
    val statementDate: Calendar,
    val paymentDueDate: Calendar
)
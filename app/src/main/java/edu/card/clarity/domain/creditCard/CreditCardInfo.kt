package edu.card.clarity.domain.creditCard

import java.util.Date

open class CreditCardInfo(
    val name: String,
    val statementDate: Date,
    val paymentDueDate: Date
)
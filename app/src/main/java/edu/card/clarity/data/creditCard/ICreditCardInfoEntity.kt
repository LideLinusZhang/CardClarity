package edu.card.clarity.data.creditCard

import java.util.Date
import java.util.UUID

interface ICreditCardInfoEntity {
    val id: UUID
    val name: String
    val statementDate: Date
    val paymentDueDate: Date
}

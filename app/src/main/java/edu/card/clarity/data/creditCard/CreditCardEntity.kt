package edu.card.clarity.data.creditCard

import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

abstract class CreditCardEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
) {
    abstract val name: String
    abstract val statementDate: Date
    abstract val paymentDueDate: Date
}

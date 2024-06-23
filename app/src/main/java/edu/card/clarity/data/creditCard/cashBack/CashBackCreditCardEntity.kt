package edu.card.clarity.data.creditCard.cashBack

import androidx.room.Entity
import edu.card.clarity.data.creditCard.CreditCardEntity
import java.util.Date

@Entity(tableName = "cashBackCreditCard")
data class CashBackCreditCardEntity(
    override val name: String,
    override val paymentDueDate: Date,
    override val statementDate: Date
) : CreditCardEntity()
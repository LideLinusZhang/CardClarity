package edu.card.clarity.data.creditCard.cashBack

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.card.clarity.data.creditCard.ICreditCardInfoEntity
import java.util.Date
import java.util.UUID

@Entity(tableName = "cashBackCreditCard")
data class CashBackCreditCardInfoEntity(
    @PrimaryKey override val id: UUID,
    override val name: String,
    override val paymentDueDate: Date,
    override val statementDate: Date
) : ICreditCardInfoEntity
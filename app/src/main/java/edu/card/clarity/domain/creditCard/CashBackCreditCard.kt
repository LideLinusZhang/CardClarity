package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.PercentagePurchaseReturn
import java.util.Date
import java.util.UUID

class CashBackCreditCard(
    id: UUID,
    name: String,
    statementDate: Date,
    paymentDueDate: Date,
    purchaseReturns: MutableList<PercentagePurchaseReturn> = mutableListOf()
) : CreditCard<PercentagePurchaseReturn>(id, name, statementDate, paymentDueDate, purchaseReturns
) {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseType == purchase.type }

        return purchaseReturn.getReturnAmount(purchase)
    }
}
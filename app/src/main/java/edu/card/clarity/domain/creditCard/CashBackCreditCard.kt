package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.PercentagePurchaseReturn
import java.util.Date

class CashBackCreditCard(
    name: String,
    statementDate: Date,
    paymentDueDate: Date,
    purchaseReturns: MutableList<PercentagePurchaseReturn> = mutableListOf()
) : CreditCard<PercentagePurchaseReturn>(name, statementDate, paymentDueDate, purchaseReturns
) {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseTypes.contains(purchase.type) }

        return purchaseReturn.getReturnAmount(purchase)
    }
}
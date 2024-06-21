package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.MultiplierPurchaseReturn
import java.util.Date

class PointBackCreditCard(
    name: String,
    statementDate: Date,
    paymentDueDate: Date,
    purchaseReturns: MutableList<MultiplierPurchaseReturn> = mutableListOf(),
    val pointSystem: PointSystem
) : CreditCard<MultiplierPurchaseReturn>(
    name, statementDate, paymentDueDate, purchaseReturns
) {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        return getReturnAmountInPoint(purchase) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchase: Purchase): Int {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseTypes.contains(purchase.type) }

        return purchaseReturn.getReturnAmount(purchase).toInt()
    }
}
package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.purchaseReturn.MultiplierPurchaseReturn
import java.util.Date
import java.util.UUID

class PointBackCreditCard(
    id: UUID,
    name: String,
    statementDate: Date,
    paymentDueDate: Date,
    val pointSystem: PointSystem,
    purchaseReturns: MutableList<MultiplierPurchaseReturn> = mutableListOf(),
) : CreditCard<MultiplierPurchaseReturn>(
    id, name, statementDate, paymentDueDate, purchaseReturns
) {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        return getReturnAmountInPoint(purchase) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchase: Purchase): Int {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseType == purchase.type }

        return purchaseReturn.getReturnAmount(purchase).toInt()
    }
}
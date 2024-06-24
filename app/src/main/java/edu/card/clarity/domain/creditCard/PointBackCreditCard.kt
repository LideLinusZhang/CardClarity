package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReturn
import java.util.UUID

class PointBackCreditCard(
    override val id: UUID,
    override val info: CreditCardInfo,
    override val purchaseReturns: List<PurchaseReturn>,
    val pointSystem: PointSystem
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        return getReturnAmountInPoint(purchase) * pointSystem.pointToCashConversionRate
    }

    fun getReturnAmountInPoint(purchase: Purchase): Int {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseType == purchase.type }

        return purchaseReturn.getReturnAmount(purchase).toInt()
    }
}
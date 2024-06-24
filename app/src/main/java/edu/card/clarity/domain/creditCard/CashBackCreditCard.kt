package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReturn
import java.util.UUID

data class CashBackCreditCard(
    override val id: UUID,
    override val info: CreditCardInfo,
    override val purchaseReturns: List<PurchaseReturn>
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        val purchaseReturn =
            purchaseReturns.first { it.applicablePurchaseType == purchase.type }

        return purchaseReturn.getReturnAmount(purchase)
    }
}
package edu.card.clarity.domain.creditCard

import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import java.util.UUID

data class CashBackCreditCard(
    override val id: UUID,
    override val info: CreditCardInfo,
    override val purchaseRewards: List<PurchaseReward>
) : ICreditCard {
    override fun getReturnAmountInCash(purchase: Purchase): Float {
        val purchaseReward = purchaseRewards.first {
            it.applicablePurchaseType == purchase.type
        }

        return purchaseReward.getReturnAmount(purchase)
    }
}
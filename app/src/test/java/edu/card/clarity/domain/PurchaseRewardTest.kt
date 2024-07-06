package edu.card.clarity.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import edu.card.clarity.enums.PurchaseType
import java.util.*

class PurchaseRewardTest {

    @Test
    fun getReturnAmount() {
        val purchaseReward = PurchaseReward(
            applicablePurchaseType = PurchaseType.Groceries,
            rewardFactor = 2.0f  // 2 points per dollar
        )

        val purchase = Purchase(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )

        val expectedReturnAmount = purchase.total * purchaseReward.rewardFactor

        assertEquals(expectedReturnAmount, purchaseReward.getReturnAmount(purchase))
    }
}
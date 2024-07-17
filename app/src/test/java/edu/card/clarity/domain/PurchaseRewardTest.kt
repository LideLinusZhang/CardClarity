package edu.card.clarity.domain

import edu.card.clarity.enums.PurchaseType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.UUID

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
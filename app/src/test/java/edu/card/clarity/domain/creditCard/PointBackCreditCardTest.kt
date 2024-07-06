package edu.card.clarity.domain.creditCard

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import edu.card.clarity.domain.*
import edu.card.clarity.enums.*
import android.icu.util.Calendar
import java.util.*

class PointBackCreditCardTest {

    @Test
    fun getReturnAmountInCash() {
        val statementDate = Calendar.getInstance()
        statementDate.set(2023, Calendar.JULY, 15)

        val paymentDueDate = Calendar.getInstance()
        paymentDueDate.set(2023, Calendar.AUGUST, 5)

        val creditCardInfo = CreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 2.0f),
            PurchaseReward(PurchaseType.Fuel, 3.0f)
        )

        val pointSystem = PointSystem(
            id = UUID.randomUUID(),
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )

        val pointBackCreditCard = PointBackCreditCard(
            info = creditCardInfo,
            purchaseRewards = purchaseRewards,
            pointSystem = pointSystem
        )

        val purchase = Purchase(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = creditCardInfo.id!!
        )

        val returnAmountInCash = pointBackCreditCard.getReturnAmountInCash(purchase)

        assertEquals(2.0f, returnAmountInCash)
    }

    @Test
    fun getReturnAmountInPoint() {
        val statementDate = Calendar.getInstance()
        statementDate.set(2023, Calendar.JULY, 15)

        val paymentDueDate = Calendar.getInstance()
        paymentDueDate.set(2023, Calendar.AUGUST, 5)

        val creditCardInfo = CreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 2.0f),
            PurchaseReward(PurchaseType.Fuel, 3.0f)
        )

        val pointSystem = PointSystem(
            id = UUID.randomUUID(),
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )

        val pointBackCreditCard = PointBackCreditCard(
            info = creditCardInfo,
            purchaseRewards = purchaseRewards,
            pointSystem = pointSystem
        )

        val purchase = Purchase(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = creditCardInfo.id!!
        )

        val returnAmountInPoints = pointBackCreditCard.getReturnAmountInPoint(purchase)

        assertEquals(200, returnAmountInPoints)
    }
}
package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar
import edu.card.clarity.domain.*
import edu.card.clarity.enums.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.*
import java.util.*

class CashBackCreditCardTest {

    private val mockCalendar = mock(Calendar::class.java)
    private val mockedStaticCalendar: MockedStatic<Calendar> = mockStatic(Calendar::class.java)

    @BeforeEach
    fun beforeEach() {
        mockedStaticCalendar.`when`<Calendar> { Calendar.getInstance() }.thenReturn(mockCalendar)
    }

    @AfterEach
    fun afterEach() {
        mockedStaticCalendar.close()
    }
    @Test
    fun getReturnAmountInCash() {
        val statementDate = Calendar.getInstance()
        statementDate.set(2023, Calendar.JULY, 15)

        val paymentDueDate = Calendar.getInstance()
        paymentDueDate.set(2023, Calendar.AUGUST, 5)

        val creditCardInfo = CreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 0.02f),
            PurchaseReward(PurchaseType.Fuel, 0.03f)
        )

        val cashBackCreditCard = CashBackCreditCard(
            info = creditCardInfo,
            purchaseRewards = purchaseRewards
        )

        val purchase = Purchase(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = creditCardInfo.id!!
        )

        val returnAmount = cashBackCreditCard.getReturnAmountInCash(purchase)

        assertEquals(2.0f, returnAmount)
    }
}
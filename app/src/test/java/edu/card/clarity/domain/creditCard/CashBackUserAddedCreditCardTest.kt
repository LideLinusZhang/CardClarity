package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.PurchaseReward
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import java.util.Date
import java.util.UUID

class CashBackUserAddedCreditCardTest {

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
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true,
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 0.02f),
            PurchaseReward(PurchaseType.Gas, 0.03f)
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
            rewardAmount = 100.0f,
            creditCardId = creditCardInfo.id!!
        )

        val returnAmount = cashBackCreditCard.getReturnAmountInCash(purchase)

        assertEquals(2.0f, returnAmount)
    }
}
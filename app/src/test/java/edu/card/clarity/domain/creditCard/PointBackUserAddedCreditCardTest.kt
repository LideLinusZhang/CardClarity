package edu.card.clarity.domain.creditCard

import android.icu.util.Calendar
import edu.card.clarity.domain.PointSystem
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
import java.util.UUID

class PointBackUserAddedCreditCardTest {

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
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true,
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 2.0f),
            PurchaseReward(PurchaseType.Gas, 3.0f)
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

        val returnAmountInCash = pointBackCreditCard.getReturnAmountInCash(100.0f, PurchaseType.Groceries)

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
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true,
        )

        val purchaseRewards = listOf(
            PurchaseReward(PurchaseType.Groceries, 2.0f),
            PurchaseReward(PurchaseType.Gas, 3.0f)
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

        val returnAmountInPoints = pointBackCreditCard.getReturnAmountInPoint(100.0f, PurchaseType.Groceries)

        assertEquals(200, returnAmountInPoints)
    }
}
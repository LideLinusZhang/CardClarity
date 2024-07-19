package edu.card.clarity.repositories.creditCard

import android.icu.util.Calendar
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCard
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCardInfo
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseReward
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.enums.RewardType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.UUID

class CashBackUserAddedCreditCardRepositoryTest {

    private lateinit var creditCardDao: CreditCardDao
    private lateinit var purchaseRewardDao: PurchaseRewardDao
    private lateinit var cashBackCreditCardRepository: CashBackCreditCardRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockCalendar = mock(Calendar::class.java)
    private val mockedStaticCalendar: MockedStatic<Calendar> = mockStatic(Calendar::class.java)

    @BeforeEach
    fun setUp() {
        creditCardDao = mock(CreditCardDao::class.java)
        purchaseRewardDao = mock(PurchaseRewardDao::class.java)
        cashBackCreditCardRepository = CashBackCreditCardRepository(
            creditCardDao,
            purchaseRewardDao,
            testDispatcher
        )
        mockedStaticCalendar.`when`<Calendar> { Calendar.getInstance() }.thenReturn(mockCalendar)
    }

    @AfterEach
    fun afterEach() {
        mockedStaticCalendar.close()
    }

    @Test
    fun addPurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseTypes = listOf(PurchaseType.Groceries)
        val percentage = 0.05f

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.CashBack)

        cashBackCreditCardRepository.addPurchaseReward(creditCardId, purchaseTypes, percentage)

        verify(purchaseRewardDao, times(1)).upsert(
            PurchaseReward(
                creditCardId = creditCardId,
                purchaseType = PurchaseType.Groceries,
                rewardType = RewardType.CashBack,
                factor = percentage
            )
        )
    }

    @Test
    fun updatePurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()

        val spyRepository = spy(cashBackCreditCardRepository)
        doReturn(Unit).`when`(spyRepository).addPurchaseReward(
            any(),
            any(),
            any()
        )

        spyRepository.updatePurchaseReward(creditCardId, listOf(PurchaseType.Groceries), 0.05f)

        verify(spyRepository, times(1)).addPurchaseReward(
            creditCardId,
            listOf(PurchaseType.Groceries),
            0.05f
        )
    }

    @Test
    fun removePurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseTypes = listOf(PurchaseType.Groceries)

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.CashBack)

        cashBackCreditCardRepository.removePurchaseReward(creditCardId, purchaseTypes)

        verify(purchaseRewardDao, times(1)).delete(creditCardId, PurchaseType.Groceries)
    }

    @Test
    fun updateCreditCardInfo() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val statementDate = Calendar.getInstance().apply { set(2023, Calendar.JULY, 15) }
        val paymentDueDate = Calendar.getInstance().apply { set(2023, Calendar.AUGUST, 5) }

        val creditCardInfo = CreditCardInfo(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true
        )

        val creditCardInfoEntity = edu.card.clarity.data.creditCard.CreditCardInfo(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true
        )

        `when`(creditCardDao.getRewardTypeById(creditCardInfo.id!!)).thenReturn(RewardType.CashBack)
        `when`(creditCardDao.getInfoById(creditCardInfo.id!!)).thenReturn(creditCardInfoEntity)

        cashBackCreditCardRepository.updateCreditCardInfo(creditCardInfo)

        verify(creditCardDao, times(1)).upsert(creditCardInfoEntity)
    }

    @Test
    fun getCreditCard() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val creditCardInfoEntity = UserAddedCreditCardInfo(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseReward = PurchaseReward(
            creditCardId = creditCardId,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.getById(creditCardId)).thenReturn(
            UserAddedCreditCard(
                creditCardInfo = creditCardInfoEntity,
                purchaseRewards = listOf(purchaseReward)
            )
        )

        val result = cashBackCreditCardRepository.getCreditCard(creditCardId)

        assertNotNull(result)
        assertTrue(result is CashBackCreditCard)
        assertEquals("Test Card", result!!.info.name)
    }

    @Test
    fun getCreditCardInfo() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val creditCardInfoEntity = edu.card.clarity.data.creditCard.CreditCardInfo(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.getInfoById(creditCardId)).thenReturn(creditCardInfoEntity)

        val result = cashBackCreditCardRepository.getCreditCardInfo(creditCardId)

        assertNotNull(result)
        assertTrue(result is CreditCardInfo)
        assertEquals("Test Card", result!!.name)
    }


    @Test
    fun getAllCreditCards() = testScope.runTest {
        val creditCardInfoEntity1 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseReward1 = PurchaseReward(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        val creditCardInfoEntity2 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseReward2 = PurchaseReward(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.getAllOf(RewardType.CashBack)).thenReturn(
            listOf(
                UserAddedCreditCard(
                    creditCardInfo = creditCardInfoEntity1,
                    purchaseRewards = listOf(purchaseReward1)
                ),
                UserAddedCreditCard(
                    creditCardInfo = creditCardInfoEntity2,
                    purchaseRewards = listOf(purchaseReward2)
                )
            )
        )

        val result = cashBackCreditCardRepository.getAllCreditCards()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].info.name)
        assertEquals("Test Card 2", result[1].info.name)
    }

    @Test
    fun getAllCreditCardInfo() = testScope.runTest {
        val creditCardInfoEntity1 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        val creditCardInfoEntity2 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.getAllInfoOf(RewardType.CashBack)).thenReturn(
            listOf(creditCardInfoEntity1, creditCardInfoEntity2)
        )

        val result = cashBackCreditCardRepository.getAllCreditCardInfo()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].name)
        assertEquals("Test Card 2", result[1].name)
    }

    @Test
    fun getAllCreditCardsStream() = testScope.runTest {
        val creditCardInfoEntity1 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseReward1 = PurchaseReward(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        val creditCardInfoEntity2 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseReward2 = PurchaseReward(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.observeAllOf(RewardType.CashBack)).thenReturn(
            flowOf(
                listOf(
                    UserAddedCreditCard(
                        creditCardInfo = creditCardInfoEntity1,
                        purchaseRewards = listOf(purchaseReward1)
                    ),
                    UserAddedCreditCard(
                        creditCardInfo = creditCardInfoEntity2,
                        purchaseRewards = listOf(purchaseReward2)
                    )
                )
            )
        )

        val result = cashBackCreditCardRepository.getAllCreditCardsStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].info.name)
        assertEquals("Test Card 2", result[1].info.name)
    }

    @Test
    fun getAllCreditCardInfoStream() = testScope.runTest {
        val creditCardInfoEntity1 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val creditCardInfoEntity2 = UserAddedCreditCardInfo(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.observeAllInfoOf(RewardType.CashBack)).thenReturn(
            flowOf(listOf(creditCardInfoEntity1, creditCardInfoEntity2))
        )

        val result = cashBackCreditCardRepository.getAllCreditCardInfoStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].name)
        assertEquals("Test Card 2", result[1].name)
    }

    @Test
    fun deleteAllCreditCards() = testScope.runTest {
        cashBackCreditCardRepository.deleteAllCreditCards()

        verify(creditCardDao, times(1)).deleteAllOf(RewardType.CashBack)
    }

    @Test
    fun deleteCreditCard() = testScope.runTest {
        val creditCardId = UUID.randomUUID()

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.CashBack)

        cashBackCreditCardRepository.deleteCreditCard(creditCardId)

        verify(creditCardDao, times(1)).deleteById(creditCardId)
    }
}
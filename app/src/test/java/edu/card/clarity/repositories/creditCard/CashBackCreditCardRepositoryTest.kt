package edu.card.clarity.repositories.creditCard

import android.icu.util.Calendar
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardEntity
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity
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
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.util.*

class CashBackCreditCardRepositoryTest {

    private lateinit var creditCardDao: CreditCardDao
    private lateinit var purchaseRewardDao: PurchaseRewardDao
    private lateinit var cashBackCreditCardRepository: CashBackCreditCardRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        creditCardDao = mock(CreditCardDao::class.java)
        purchaseRewardDao = mock(PurchaseRewardDao::class.java)
        cashBackCreditCardRepository = CashBackCreditCardRepository(
            creditCardDao,
            purchaseRewardDao,
            testDispatcher
        )
    }

    @Test
    fun addPurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseTypes = listOf(PurchaseType.Groceries)
        val percentage = 0.05f

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.CashBack)

        cashBackCreditCardRepository.addPurchaseReward(creditCardId, purchaseTypes, percentage)

        verify(purchaseRewardDao, times(1)).upsert(PurchaseRewardEntity(
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
        doNothing().`when`(spyRepository).addPurchaseReward(
            eq(creditCardId),
            anyList(),
            anyFloat()
        )

        spyRepository.updatePurchaseReward(creditCardId, listOf(PurchaseType.Groceries), 2.0f)

        verify(spyRepository, times(1)).addPurchaseReward(creditCardId, listOf(PurchaseType.Groceries), 2.0f)
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
            paymentDueDate = paymentDueDate
        )

        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate
        )

        `when`(creditCardDao.getRewardTypeById(creditCardInfo.id!!)).thenReturn(RewardType.CashBack)
        `when`(creditCardDao.getInfoById(creditCardInfo.id!!)).thenReturn(creditCardInfoEntity)

        cashBackCreditCardRepository.updateCreditCardInfo(creditCardInfo)

        verify(creditCardDao, times(1)).upsert(creditCardInfoEntity)
    }

    @Test
    fun getCreditCard() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val purchaseRewardEntity = PurchaseRewardEntity(
            creditCardId = creditCardId,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.getById(creditCardId)).thenReturn(
            CreditCardEntity(
                creditCardInfo = creditCardInfoEntity,
                purchaseRewards = listOf(purchaseRewardEntity)
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
        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )

        `when`(creditCardDao.getInfoById(creditCardId)).thenReturn(creditCardInfoEntity)

        val result = cashBackCreditCardRepository.getCreditCardInfo(creditCardId)

        assertNotNull(result)
        assertTrue(result is CreditCardInfo)
        assertEquals("Test Card", result!!.name)
    }


    @Test
    fun getAllCreditCards() = testScope.runTest {
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val purchaseRewardEntity1 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val purchaseRewardEntity2 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.getAllOf(RewardType.CashBack)).thenReturn(
            listOf(
                CreditCardEntity(
                    creditCardInfo = creditCardInfoEntity1,
                    purchaseRewards = listOf(purchaseRewardEntity1)
                ),
                CreditCardEntity(
                    creditCardInfo = creditCardInfoEntity2,
                    purchaseRewards = listOf(purchaseRewardEntity2)
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
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
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
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val purchaseRewardEntity1 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val purchaseRewardEntity2 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.CashBack,
            factor = 0.05f
        )

        `when`(creditCardDao.observeAllOf(RewardType.CashBack)).thenReturn(
            flowOf(
                listOf(
                    CreditCardEntity(
                        creditCardInfo = creditCardInfoEntity1,
                        purchaseRewards = listOf(purchaseRewardEntity1)
                    ),
                    CreditCardEntity(
                        creditCardInfo = creditCardInfoEntity2,
                        purchaseRewards = listOf(purchaseRewardEntity2)
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
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.CashBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
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
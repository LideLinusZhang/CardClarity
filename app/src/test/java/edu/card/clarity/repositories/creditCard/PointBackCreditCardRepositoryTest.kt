package edu.card.clarity.repositories.creditCard

import android.icu.util.Calendar
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardEntity
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.creditCard.pointBack.CreditCardIdPointSystemIdPairEntity
import edu.card.clarity.data.creditCard.pointBack.CreditCardPointSystemAssociation
import edu.card.clarity.data.creditCard.pointBack.PointBackCardPointSystemAssociationDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.purchaseReward.PurchaseRewardDao
import edu.card.clarity.data.purchaseReward.PurchaseRewardEntity
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.PointBackCreditCard
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

class PointBackCreditCardRepositoryTest {

    private lateinit var creditCardDao: CreditCardDao
    private lateinit var purchaseRewardDao: PurchaseRewardDao
    private lateinit var pointSystemAssociationDao: PointBackCardPointSystemAssociationDao
    private lateinit var pointBackCreditCardRepository: PointBackCreditCardRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockCalendar = mock(Calendar::class.java)
    private val mockedStaticCalendar: MockedStatic<Calendar> = mockStatic(Calendar::class.java)

    @BeforeEach
    fun setUp() {
        creditCardDao = mock(CreditCardDao::class.java)
        purchaseRewardDao = mock(PurchaseRewardDao::class.java)
        pointSystemAssociationDao = mock(PointBackCardPointSystemAssociationDao::class.java)
        pointBackCreditCardRepository = PointBackCreditCardRepository(
            creditCardDao,
            purchaseRewardDao,
            pointSystemAssociationDao,
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
        val multiplier = 2.0f

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.PointBack)

        pointBackCreditCardRepository.addPurchaseReward(creditCardId, purchaseTypes, multiplier)

        verify(purchaseRewardDao, times(1)).upsert(
            PurchaseRewardEntity(
                creditCardId = creditCardId,
                purchaseType = PurchaseType.Groceries,
                rewardType = RewardType.PointBack,
                factor = multiplier
            )
        )
    }

    @Test
    fun updatePurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()

        val spyRepository = spy(pointBackCreditCardRepository)
        doReturn(Unit).`when`(spyRepository).addPurchaseReward(
            any(),
            any(),
            any()
        )

        spyRepository.updatePurchaseReward(creditCardId, listOf(PurchaseType.Groceries), 2.0f)

        verify(spyRepository, times(1)).addPurchaseReward(
            creditCardId,
            listOf(PurchaseType.Groceries),
            2.0f
        )
    }

    @Test
    fun removePurchaseReward() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseTypes = listOf(PurchaseType.Groceries)

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.PointBack)

        pointBackCreditCardRepository.removePurchaseReward(creditCardId, purchaseTypes)

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
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true
        )

        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = statementDate,
            paymentDueDate = paymentDueDate,
            isReminderEnabled = true
        )

        `when`(creditCardDao.getRewardTypeById(creditCardInfo.id!!)).thenReturn(RewardType.PointBack)
        `when`(creditCardDao.getInfoById(creditCardInfo.id!!)).thenReturn(creditCardInfoEntity)

        pointBackCreditCardRepository.updateCreditCardInfo(creditCardInfo)

        verify(creditCardDao, times(1)).upsert(creditCardInfoEntity)
    }

    @Test
    fun getCreditCard() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val pointSystemId = UUID.randomUUID()
        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseRewardEntity = PurchaseRewardEntity(
            creditCardId = creditCardId,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.PointBack,
            factor = 2.0f
        )
        val pointSystemEntity = PointSystemEntity(
            id = pointSystemId,
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )

        `when`(creditCardDao.getById(creditCardId)).thenReturn(
            CreditCardEntity(
                creditCardInfo = creditCardInfoEntity,
                purchaseRewards = listOf(purchaseRewardEntity)
            )
        )
        `when`(pointSystemAssociationDao.getByCreditCardId(creditCardId)).thenReturn(
            CreditCardPointSystemAssociation(
                idPair = CreditCardIdPointSystemIdPairEntity(
                    creditCardId = creditCardId,
                    pointSystemId = pointSystemId
                ),
                pointSystem = pointSystemEntity
            )
        )

        val result = pointBackCreditCardRepository.getCreditCard(creditCardId)

        assertNotNull(result)
        assertTrue(result is PointBackCreditCard)
        assertEquals("Test Card", result!!.info.name)
        assertEquals("Test Point System", result.pointSystem.name)
    }

    @Test
    fun getCreditCardInfo() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val creditCardInfoEntity = CreditCardInfoEntity(
            id = creditCardId,
            name = "Test Card",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.getInfoById(creditCardId)).thenReturn(creditCardInfoEntity)

        val result = pointBackCreditCardRepository.getCreditCardInfo(creditCardId)

        assertNotNull(result)
        assertTrue(result is CreditCardInfo)
        assertEquals("Test Card", result!!.name)
    }

    @Test
    fun getAllCreditCards() = testScope.runTest {
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 1",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseRewardEntity1 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.PointBack,
            factor = 2.0f
        )
        val pointSystemEntity1 = PointSystemEntity(
            id = UUID.randomUUID(),
            name = "Test Point System 1",
            pointToCashConversionRate = 0.01f
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseRewardEntity2 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.PointBack,
            factor = 2.0f
        )
        val pointSystemEntity2 = PointSystemEntity(
            id = UUID.randomUUID(),
            name = "Test Point System 2",
            pointToCashConversionRate = 0.01f
        )

        `when`(creditCardDao.getAllOf(RewardType.PointBack)).thenReturn(
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

        `when`(pointSystemAssociationDao.getByCreditCardId(creditCardInfoEntity1.id)).thenReturn(
            CreditCardPointSystemAssociation(
                idPair = CreditCardIdPointSystemIdPairEntity(
                    creditCardId = creditCardInfoEntity1.id,
                    pointSystemId = pointSystemEntity1.id
                ),
                pointSystem = pointSystemEntity1
            )
        )
        `when`(pointSystemAssociationDao.getByCreditCardId(creditCardInfoEntity2.id)).thenReturn(
            CreditCardPointSystemAssociation(
                idPair = CreditCardIdPointSystemIdPairEntity(
                    creditCardId = creditCardInfoEntity2.id,
                    pointSystemId = pointSystemEntity2.id
                ),
                pointSystem = pointSystemEntity2
            )
        )

        val result = pointBackCreditCardRepository.getAllCreditCards()

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
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.getAllInfoOf(RewardType.PointBack)).thenReturn(
            listOf(creditCardInfoEntity1, creditCardInfoEntity2)
        )

        val result = pointBackCreditCardRepository.getAllCreditCardInfo()

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
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseRewardEntity1 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity1.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.PointBack,
            factor = 2.0f
        )
        val pointSystemEntity1 = PointSystemEntity(
            id = UUID.randomUUID(),
            name = "Test Point System 1",
            pointToCashConversionRate = 0.01f
        )

        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val purchaseRewardEntity2 = PurchaseRewardEntity(
            creditCardId = creditCardInfoEntity2.id,
            purchaseType = PurchaseType.Groceries,
            rewardType = RewardType.PointBack,
            factor = 2.0f
        )
        val pointSystemEntity2 = PointSystemEntity(
            id = UUID.randomUUID(),
            name = "Test Point System 2",
            pointToCashConversionRate = 0.01f
        )

        `when`(creditCardDao.observeAllOf(RewardType.PointBack)).thenReturn(
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
        `when`(pointSystemAssociationDao.getByCreditCardId(creditCardInfoEntity1.id)).thenReturn(
            CreditCardPointSystemAssociation(
                idPair = CreditCardIdPointSystemIdPairEntity(
                    creditCardId = creditCardInfoEntity1.id,
                    pointSystemId = pointSystemEntity1.id
                ),
                pointSystem = pointSystemEntity1
            )
        )
        `when`(pointSystemAssociationDao.getByCreditCardId(creditCardInfoEntity2.id)).thenReturn(
            CreditCardPointSystemAssociation(
                idPair = CreditCardIdPointSystemIdPairEntity(
                    creditCardId = creditCardInfoEntity2.id,
                    pointSystemId = pointSystemEntity2.id
                ),
                pointSystem = pointSystemEntity2
            )
        )

        val result = pointBackCreditCardRepository.getAllCreditCardsStream().first()

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
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )
        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = UUID.randomUUID(),
            name = "Test Card 2",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance(),
            isReminderEnabled = true
        )

        `when`(creditCardDao.observeAllInfoOf(RewardType.PointBack)).thenReturn(
            flowOf(listOf(creditCardInfoEntity1, creditCardInfoEntity2))
        )

        val result = pointBackCreditCardRepository.getAllCreditCardInfoStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].name)
        assertEquals("Test Card 2", result[1].name)
    }

    @Test
    fun deleteAllCreditCards() = testScope.runTest {
        pointBackCreditCardRepository.deleteAllCreditCards()

        verify(creditCardDao, times(1)).deleteAllOf(RewardType.PointBack)
    }

    @Test
    fun deleteCreditCard() = testScope.runTest {
        val creditCardId = UUID.randomUUID()

        `when`(creditCardDao.getRewardTypeById(creditCardId)).thenReturn(RewardType.PointBack)

        pointBackCreditCardRepository.deleteCreditCard(creditCardId)

        verify(creditCardDao, times(1)).deleteById(creditCardId)
    }
}
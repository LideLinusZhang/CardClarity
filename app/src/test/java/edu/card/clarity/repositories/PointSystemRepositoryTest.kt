package edu.card.clarity.repositories

import android.icu.util.Calendar
import edu.card.clarity.data.creditCard.CreditCardDao
import edu.card.clarity.data.creditCard.CreditCardInfoEntity
import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.data.pointSystem.PointSystemWithCreditCardIds
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.enums.CardNetworkType
import edu.card.clarity.enums.RewardType
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PointSystemRepositoryTest {

    private lateinit var pointSystemDao: PointSystemDao
    private lateinit var creditCardDao: CreditCardDao
    private lateinit var pointSystemRepository: PointSystemRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        pointSystemDao = mock(PointSystemDao::class.java)
        creditCardDao = mock(CreditCardDao::class.java)
        pointSystemRepository = PointSystemRepository(
            pointSystemDao,
            creditCardDao,
            testDispatcher
        )
    }

    @Test
    fun addPointSystem() = testScope.runTest {
        val pointSystem = PointSystem(
            name = "Test System",
            pointToCashConversionRate = 0.01f
        )

        `when`(pointSystemDao.upsert(any(PointSystemEntity::class.java))).thenReturn(Unit)

        val result = pointSystemRepository.addPointSystem(pointSystem)

        assertNotNull(result)
        verify(pointSystemDao, times(1)).upsert(any(PointSystemEntity::class.java))
    }

    @Test
    fun getPointSystem() = testScope.runTest {
        val pointSystemId = UUID.randomUUID()
        val pointSystemEntity = PointSystemEntity(
            id = pointSystemId,
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )

        `when`(pointSystemDao.getById(pointSystemId)).thenReturn(pointSystemEntity)

        val result = pointSystemRepository.getPointSystem(pointSystemId)

        assertNotNull(result)
        assertEquals(pointSystemId, result?.id)
        assertEquals("Test Point System", result?.name)
    }

    @Test
    fun getAllPointSystems() = testScope.runTest {
        val pointSystemId1 = UUID.randomUUID()
        val pointSystemEntity1 = PointSystemEntity(
            id = pointSystemId1,
            name = "Test Point System 1",
            pointToCashConversionRate = 0.01f
        )
        val pointSystemId2 = UUID.randomUUID()
        val pointSystemEntity2 = PointSystemEntity(
            id = pointSystemId2,
            name = "Test Point System 2",
            pointToCashConversionRate = 0.01f
        )

        `when`(pointSystemDao.getAll()).thenReturn(listOf(pointSystemEntity1, pointSystemEntity2))

        val result = pointSystemRepository.getAllPointSystems()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Point System 1", result[0].name)
        assertEquals("Test Point System 2", result[1].name)
    }

    @Test
    fun getAllPointSystemsStream() = testScope.runTest {
        val pointSystemId1 = UUID.randomUUID()
        val pointSystemEntity1 = PointSystemEntity(
            id = pointSystemId1,
            name = "Test Point System 1",
            pointToCashConversionRate = 0.01f
        )
        val pointSystemId2 = UUID.randomUUID()
        val pointSystemEntity2 = PointSystemEntity(
            id = pointSystemId2,
            name = "Test Point System 2",
            pointToCashConversionRate = 0.01f
        )

        `when`(pointSystemDao.observeAll()).thenReturn(flowOf(listOf(pointSystemEntity1, pointSystemEntity2)))
        val result = pointSystemRepository.getAllPointSystemsStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Point System 1", result[0].name)
        assertEquals("Test Point System 2", result[1].name)
    }

    @Test
    fun getCreditCardsUsingPointSystem() = testScope.runTest {
        val pointSystemId = UUID.randomUUID()
        val creditCardId1 = UUID.randomUUID()
        val creditCardInfoEntity1 = CreditCardInfoEntity(
            id = creditCardId1,
            name = "Test Card 1",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val creditCardId2 = UUID.randomUUID()
        val creditCardInfoEntity2 = CreditCardInfoEntity(
            id = creditCardId2,
            name = "Test Card 2",
            rewardType = RewardType.PointBack,
            cardNetworkType = CardNetworkType.Visa,
            statementDate = Calendar.getInstance(),
            paymentDueDate = Calendar.getInstance()
        )
        val pointSystemEntity = PointSystemEntity(
            id = pointSystemId,
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )
        val pointSystemWithCreditCardIds = PointSystemWithCreditCardIds(
            pointSystem = pointSystemEntity,
            creditCardIds = listOf(creditCardId1, creditCardId2)
        )

        `when`(pointSystemDao.getPointSystemWithCreditCards(pointSystemId)).thenReturn(pointSystemWithCreditCardIds )
        `when`(creditCardDao.getInfoById(creditCardId1)).thenReturn(creditCardInfoEntity1)
        `when`(creditCardDao.getInfoById(creditCardId2)).thenReturn(creditCardInfoEntity2)

        val result = pointSystemRepository.getCreditCardsUsingPointSystem(pointSystemId)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("Test Card 1", result[0].name)
        assertEquals("Test Card 2", result[1].name)
    }

    @Test
    fun updatePointSystem() = testScope.runTest {
        val pointSystemId = UUID.randomUUID()
        val pointSystem = PointSystem(
            id = pointSystemId,
            name = "Test Point System",
            pointToCashConversionRate = 0.01f
        )

        `when`(pointSystemDao.exist(pointSystemId)).thenReturn(true)

        pointSystemRepository.updatePointSystem(pointSystem)

        verify(pointSystemDao, times(1)).upsert(any(PointSystemEntity::class.java))
    }

    @Test
    fun removePointSystem() = testScope.runTest {
        val pointSystemId = UUID.randomUUID()

        pointSystemRepository.removePointSystem(pointSystemId)

        verify(pointSystemDao, times(1)).deleteById(pointSystemId)
    }

    @Test
    fun removeAllPointSystems() = testScope.runTest {
        pointSystemRepository.removeAllPointSystems()

        verify(pointSystemDao, times(1)).deleteAll()
    }
}
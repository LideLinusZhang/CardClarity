package edu.card.clarity.repositories

import android.icu.util.Calendar
import edu.card.clarity.data.purchase.PurchaseDao
import edu.card.clarity.data.purchase.PurchaseEntity
import edu.card.clarity.domain.Purchase
import edu.card.clarity.enums.PurchaseType
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date
import java.util.UUID

class PurchaseRepositoryTest {
    private lateinit var purchaseDao: PurchaseDao
    private lateinit var purchaseRepository: PurchaseRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockCalendar = mock(Calendar::class.java)
    private val mockedStaticCalendar: MockedStatic<Calendar> = mockStatic(Calendar::class.java)

    @BeforeEach
    fun setUp() {
        purchaseDao = mock(PurchaseDao::class.java)
        purchaseRepository = PurchaseRepository(purchaseDao, testDispatcher)
        mockedStaticCalendar.`when`<Calendar> { Calendar.getInstance() }.thenReturn(mockCalendar)
    }

    @AfterEach
    fun afterEach() {
        mockedStaticCalendar.close()
    }

    @Test
    fun addPurchase() = testScope.runTest {
        val purchase = Purchase(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )

        `when`(purchaseDao.upsert(any())).thenReturn(Unit)

        val result = purchaseRepository.addPurchase(purchase)

        assertNotNull(result)
        verify(purchaseDao, times(1)).upsert(any())
    }

    @Test
    fun getPurchase() = testScope.runTest {
        val purchaseId = UUID.randomUUID()
        val purchaseEntity = PurchaseEntity(
            id = purchaseId,
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )

        `when`(purchaseDao.getById(purchaseId)).thenReturn(purchaseEntity)

        val result = purchaseRepository.getPurchase(purchaseId)

        assertNotNull(result)
        assertEquals(purchaseId, result?.id)
        assertEquals("T&T", result?.merchant)
    }

    @Test
    fun getAllPurchases() = testScope.runTest {
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(purchaseDao.getAll()).thenReturn(purchaseEntities)

        val result = purchaseRepository.getAllPurchases()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllPurchasesOf() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = creditCardId
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = creditCardId
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(purchaseDao.getAllOf(creditCardId)).thenReturn(purchaseEntities)

        val result = purchaseRepository.getAllPurchasesOf(creditCardId)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllPurchasesBetween() = testScope.runTest {
        val startTime = Date()
        val endTime = Date(startTime.time + 100000)
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = startTime,
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = endTime,
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(purchaseDao.getAllBetween(startTime, endTime)).thenReturn(purchaseEntities)

        val result = purchaseRepository.getAllPurchasesBetween(startTime, endTime)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllPurchasesStream() = testScope.runTest {
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(purchaseDao.observeAll()).thenReturn(flowOf(purchaseEntities))

        val result = purchaseRepository.getAllPurchasesStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllPurchasesStreamOf() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = creditCardId
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = Date(),
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = creditCardId
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(purchaseDao.observeAllOf(creditCardId)).thenReturn(flowOf(purchaseEntities))

        val result = purchaseRepository.getAllPurchasesStreamOf(creditCardId).first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllPurchasesStreamBetween() = testScope.runTest {
        val startTime = Date()
        val endTime = Date(startTime.time + 100000)
        val purchaseEntity1 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = startTime,
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntity2 = PurchaseEntity(
            id = UUID.randomUUID(),
            time = endTime,
            merchant = "Costco",
            type = PurchaseType.Gas,
            total = 200.0f,
            creditCardId = UUID.randomUUID()
        )
        val purchaseEntities = listOf(
            purchaseEntity1,
            purchaseEntity2
        )

        `when`(
            purchaseDao.observeAllBetween(
                startTime,
                endTime
            )
        ).thenReturn(flowOf(purchaseEntities))

        val result = purchaseRepository.getAllPurchasesStreamBetween(startTime, endTime).first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun updatePurchase() = testScope.runTest {
        val purchaseId = UUID.randomUUID()
        val purchase = Purchase(
            id = purchaseId,
            time = Date(),
            merchant = "T&T",
            type = PurchaseType.Groceries,
            total = 100.0f,
            creditCardId = UUID.randomUUID()
        )

        `when`(purchaseDao.exist(purchaseId)).thenReturn(true)

        purchaseRepository.updatePurchase(purchase)

        verify(purchaseDao, times(1)).upsert(
            PurchaseEntity(
                id = purchaseId,
                time = purchase.time,
                merchant = "T&T",
                type = PurchaseType.Groceries,
                total = 100.0f,
                creditCardId = purchase.creditCardId
            )
        )
    }

    @Test
    fun removePurchase() = testScope.runTest {
        val purchaseId = UUID.randomUUID()

        purchaseRepository.removePurchase(purchaseId)

        verify(purchaseDao, times(1)).deleteById(purchaseId)
    }
}
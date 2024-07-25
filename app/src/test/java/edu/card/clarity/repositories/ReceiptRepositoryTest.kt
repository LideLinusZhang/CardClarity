package edu.card.clarity.repositories

import edu.card.clarity.data.receipt.ReceiptDao
import edu.card.clarity.data.receipt.Receipt as ReceiptInDB
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
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.UUID

class ReceiptRepositoryTest {
    private lateinit var receiptDao: ReceiptDao
    private lateinit var receiptRepository: ReceiptRepository

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        receiptDao = mock(ReceiptDao::class.java)
        receiptRepository = ReceiptRepository(receiptDao, testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        // Cleanup if necessary
    }

    @Test
    fun getReceipt() = testScope.runTest {
        val receiptId = UUID.randomUUID()
        val receipt = ReceiptInDB(
            id = receiptId,
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Groceries",
            photoPath = "path"
        )

        `when`(receiptDao.getById(receiptId)).thenReturn(receipt)

        val result = receiptRepository.getReceipt(receiptId)

        assertNotNull(result)
        assertEquals(receiptId, result?.id)
        assertEquals("2024-07-14", result?.date)
        assertEquals("100.0", result?.totalAmount)
        assertEquals("T&T", result?.merchant)
        assertEquals("Groceries", result?.selectedPurchaseType)
        assertEquals("path", result?.photoPath)
    }

    @Test
    fun getAllReceipts() = testScope.runTest {
        val receipt1 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Groceries",
            photoPath = "path1"
        )
        val receipt2 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-08-06",
            totalAmount = "200.0",
            merchant = "Costco",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Gas",
            photoPath = "path2"
        )
        val receiptEntities = listOf(
            receipt1,
            receipt2
        )

        `when`(receiptDao.getAll()).thenReturn(receiptEntities)

        val result = receiptRepository.getAllReceipts()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllReceiptsStream() = testScope.runTest {
        val receipt1 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Groceries",
            photoPath = "path1"
        )
        val receipt2 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-08-06",
            totalAmount = "200.0",
            merchant = "Costco",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Gas",
            photoPath = "path2"
        )
        val receiptEntities = listOf(
            receipt1,
            receipt2
        )

        `when`(receiptDao.observeAll()).thenReturn(flowOf(receiptEntities))

        val result = receiptRepository.getAllReceiptsStream().first()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllReceiptsOf() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val receipt1 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = creditCardId,
            selectedPurchaseType = "Groceries",
            photoPath = "path1"
        )
        val receipt2 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-08-06",
            totalAmount = "200.0",
            merchant = "Costco",
            selectedCardId = creditCardId,
            selectedPurchaseType = "Gas",
            photoPath = "path2"
        )
        val receiptEntities = listOf(
            receipt1,
            receipt2
        )

        `when`(receiptDao.getAllOf(creditCardId)).thenReturn(receiptEntities)

        val result = receiptRepository.getAllReceiptsOf(creditCardId)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllReceiptsOfType() = testScope.runTest {
        val purchaseType = PurchaseType.Groceries
        val receipt1 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Groceries",
            photoPath = "path1"
        )
        val receipt2 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-08-06",
            totalAmount = "200.0",
            merchant = "Costco",
            selectedCardId = UUID.randomUUID(),
            selectedPurchaseType = "Groceries",
            photoPath = "path2"
        )
        val receiptEntities = listOf(
            receipt1,
            receipt2
        )

        `when`(receiptDao.getAllOfType(purchaseType)).thenReturn(receiptEntities)

        val result = receiptRepository.getAllReceiptsOfType(purchaseType)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun getAllReceiptsOfCreditCardAndType() = testScope.runTest {
        val creditCardId = UUID.randomUUID()
        val purchaseType = PurchaseType.Groceries
        val receipt1 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-07-14",
            totalAmount = "100.0",
            merchant = "T&T",
            selectedCardId = creditCardId,
            selectedPurchaseType = "Groceries",
            photoPath = "path1"
        )
        val receipt2 = ReceiptInDB(
            id = UUID.randomUUID(),
            date = "2024-08-06",
            totalAmount = "200.0",
            merchant = "Costco",
            selectedCardId = creditCardId,
            selectedPurchaseType = "Groceries",
            photoPath = "path2"
        )
        val receiptEntities = listOf(
            receipt1,
            receipt2
        )

        `when`(receiptDao.getAllByCreditCardAndPurchaseType(creditCardId, purchaseType)).thenReturn(receiptEntities)

        val result = receiptRepository.getAllReceiptsOfCreditCardAndType(creditCardId, purchaseType)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
        assertEquals("T&T", result[0].merchant)
        assertEquals("Costco", result[1].merchant)
    }

    @Test
    fun removeReceipt() = testScope.runTest {
        val receiptId = UUID.randomUUID()

        receiptRepository.removeReceipt(receiptId)

        verify(receiptDao, times(1)).deleteById(receiptId)
    }
}
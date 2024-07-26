package edu.card.clarity.repositories

import edu.card.clarity.data.receipt.ReceiptDao
import edu.card.clarity.dependencyInjection.annotations.DefaultDispatcher
import edu.card.clarity.domain.Purchase
import edu.card.clarity.domain.Receipt
import edu.card.clarity.enums.PurchaseType
import edu.card.clarity.repositories.utils.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptRepository @Inject constructor(
    private val receiptDataSource: ReceiptDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getReceipt(id: UUID): Receipt? {
        return receiptDataSource.getById(id)?.toDomainModel()
    }

    suspend fun getAllReceipts(): List<Receipt> {
        return receiptDataSource.getAll().toDomainModel()
    }

    fun getAllReceiptsStream(): Flow<List<Receipt>> {
        return receiptDataSource.observeAll().map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    suspend fun getAllReceiptsOf(creditCardId: UUID): List<Receipt> {
        return receiptDataSource.getAllOf(creditCardId).toDomainModel()
    }

    suspend fun getAllReceiptsOfType(purchaseType: PurchaseType): List<Receipt> {
        return receiptDataSource.getAllOfType(purchaseType).toDomainModel()
    }

    suspend fun getAllReceiptsOfCreditCardAndType(creditCardId: UUID, purchaseType: PurchaseType): List<Receipt> {
        return receiptDataSource.getAllByCreditCardAndPurchaseType(creditCardId, purchaseType).toDomainModel()
    }

    suspend fun removeReceipt(id: UUID) {
        receiptDataSource.deleteById(id)
    }
}
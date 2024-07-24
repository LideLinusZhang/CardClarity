package edu.card.clarity.data.receipt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.card.clarity.enums.PurchaseType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ReceiptDao {

    @Insert
    suspend fun insertReceipt(receipt: Receipt)

    @Query("SELECT * FROM receipts")
    fun observeAll(): Flow<List<Receipt>>

    @Query("DELETE FROM receipts WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getById(id: UUID): Receipt?

    @Query("SELECT * FROM receipts")
    suspend fun getAll(): List<Receipt>

    @Query("SELECT * FROM receipts WHERE selectedCardId = :creditCardId")
    suspend fun getAllOf(creditCardId: UUID): List<Receipt>

    @Query("SELECT * FROM receipts WHERE selectedCardId = :purchaseType")
    suspend fun getAllOfType(purchaseType: PurchaseType): List<Receipt>

    @Query("SELECT * FROM receipts WHERE selectedCardId = :creditCardId AND selectedPurchaseType = :purchaseType")
    suspend fun getAllByCreditCardAndPurchaseType(creditCardId: UUID, purchaseType: PurchaseType): List<Receipt>
}

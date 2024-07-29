package edu.card.clarity.data.purchase.receipt

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ReceiptDao {
    /**
     * Observes a receipt by purchase id.
     *
     * @param purchaseId the purchase id.
     * @return the receipt by purchase id.
     */
    @Query("SELECT * FROM receipt WHERE purchaseId = :purchaseId")
    fun observeByPurchaseId(purchaseId: UUID): Flow<Receipt>

    /**
     * Select a receipt by purchase id.
     *
     * @param purchaseId the purchase id.
     * @return the receipt by purchase id.
     */
    @Query("SELECT * FROM receipt WHERE purchaseId = :purchaseId")
    suspend fun getByPurchaseId(purchaseId: UUID): Receipt?

    /**
     * Insert or update a receipt in the database. If a receipt already exists, replace it.
     *
     * @param receipt the receipt to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(receipt: Receipt)

    /**
     * Check if the receipt of a certain purchase id exists.
     *
     * @param purchaseId the purchase id.
     * @return true if exists, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT * FROM receipt WHERE purchaseId = :purchaseId)")
    suspend fun exist(purchaseId: UUID): Boolean

    /**
     * Delete a receipt by purchase id.
     *
     * @param purchaseId id of the purchase
     * @return the number of receipt deleted. This should always be 1.
     */
    @Query("DELETE FROM receipt WHERE purchaseId = :purchaseId")
    suspend fun deleteByPurchaseId(purchaseId: UUID): Int

    /**
     * Delete all receipts.
     */
    @Query("DELETE FROM receipt")
    suspend fun deleteAll()
}
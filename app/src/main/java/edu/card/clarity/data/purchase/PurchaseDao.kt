package edu.card.clarity.data.purchase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PurchaseDao {
    /**
     * Observes list of purchases.
     *
     * @return all purchases.
     */
    @Query("SELECT * FROM purchase")
    fun observeAll(): Flow<List<PurchaseEntity>>

    /**
     * Observes list of purchases made with a certain credit card.
     *
     * @param creditCardId credit card id
     * @return all purchases made with a certain credit card.
     */
    @Query("SELECT * FROM purchase WHERE creditCardId = :creditCardId")
    fun observeAllOf(creditCardId: UUID): Flow<List<PurchaseEntity>>

    /**
     * Observes a single purchase.
     *
     * @param id the purchase id.
     * @return the purchase with id.
     */
    @Query("SELECT * FROM purchase WHERE id = :id")
    fun observeById(id: UUID): Flow<PurchaseEntity>

    /**
     * Select all purchases.
     *
     * @return all purchases.
     */
    @Query("SELECT * FROM purchase")
    suspend fun getAll(): List<PurchaseEntity>

    /**
     * Select all purchases made with a certain credit card.
     *
     * @param creditCardId credit card id
     * @return all purchases made with a certain credit card.
     */
    @Query("SELECT * FROM purchase WHERE creditCardId = :creditCardId")
    suspend fun getAllOf(creditCardId: UUID): List<PurchaseEntity>

    /**
     * Select a purchase by id.
     *
     * @param id the purchase id.
     * @return the purchase with id.
     */
    @Query("SELECT * FROM purchase WHERE id = :id")
    suspend fun getById(id: UUID): PurchaseEntity?

    /**
     * Insert or update a purchase in the database. If a purchase already exists, replace it.
     *
     * @param purchase the purchase to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(purchase: PurchaseEntity)

    /**
     * Delete a purchase by id.
     *
     * @param id id of the purchase
     * @return the number of purchase deleted. This should always be 1.
     */
    @Query("DELETE FROM purchase WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    /**
     * Delete all purchases.
     */
    @Query("DELETE FROM purchase")
    suspend fun deleteAll()
}
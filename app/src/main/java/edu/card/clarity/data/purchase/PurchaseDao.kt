package edu.card.clarity.data.purchase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Upsert
import edu.card.clarity.data.converters.DateConverter
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

@Dao
@TypeConverters(DateConverter::class)
interface PurchaseDao {
    /**
     * Observes list of purchases.
     *
     * @return all purchases.
     */
    @Query("SELECT * FROM purchase")
    fun observeAll(): Flow<List<Purchase>>

    /**
     * Observes list of purchases made with a certain credit card.
     *
     * @param creditCardId credit card id
     * @return all purchases made with a certain credit card.
     */
    @Query("SELECT * FROM purchase WHERE creditCardId = :creditCardId")
    fun observeAllOf(creditCardId: UUID): Flow<List<Purchase>>

    /**
     * Observe all purchases made in a time period.
     *
     * @param startTime start of the period, inclusive
     * @param endTime end of the period, inclusive.
     * @return all purchases made in the specified time period.
     */
    @Query("SELECT * FROM purchase WHERE time BETWEEN :startTime AND :endTime")
    fun observeAllBetween(startTime: Date, endTime: Date): Flow<List<Purchase>>

    /**
     * Observes a single purchase.
     *
     * @param id the purchase id.
     * @return the purchase with id.
     */
    @Query("SELECT * FROM purchase WHERE id = :id")
    fun observeById(id: UUID): Flow<Purchase>

    /**
     * Select all purchases.
     *
     * @return all purchases.
     */
    @Query("SELECT * FROM purchase")
    suspend fun getAll(): List<Purchase>

    /**
     * Select all purchases made with a certain credit card.
     *
     * @param creditCardId credit card id
     * @return all purchases made with a certain credit card.
     */
    @Query("SELECT * FROM purchase WHERE creditCardId = :creditCardId")
    suspend fun getAllOf(creditCardId: UUID): List<Purchase>

    /**
     * Select all purchases made in a time period.
     *
     * @param startTime start of the period, inclusive
     * @param endTime end of the period, inclusive.
     * @return all purchases made in the specified time period.
     */
    @Query("SELECT * FROM purchase WHERE time BETWEEN :startTime AND :endTime")
    suspend fun getAllBetween(startTime: Date, endTime: Date): List<Purchase>

    /**
     * Select a purchase by id.
     *
     * @param id the purchase id.
     * @return the purchase with id.
     */
    @Query("SELECT * FROM purchase WHERE id = :id")
    suspend fun getById(id: UUID): Purchase?

    /**
     * Insert or update a purchase in the database. If a purchase already exists, replace it.
     *
     * @param purchase the purchase to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(purchase: Purchase)

    /**
     * Check if the purchase of a certain id exists.
     *
     * @param id id of the purchase.
     * @return true if exists, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT * FROM purchase WHERE id = :id)")
    suspend fun exist(id: UUID): Boolean

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
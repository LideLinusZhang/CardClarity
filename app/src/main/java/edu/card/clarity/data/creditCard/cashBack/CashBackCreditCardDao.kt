package edu.card.clarity.data.creditCard.cashBack

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CashBackCreditCardDao {
    /**
     * Observes list of systems.
     *
     * @return all cash back credit cards.
     */
    @Query("SELECT * FROM cashBackCreditCard")
    fun observeAllInfo(): Flow<List<CashBackCreditCardInfoEntity>>

    /**
     * Observes a single credit card.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM cashBackCreditCard WHERE id = :id")
    fun observeInfoById(id: UUID): Flow<CashBackCreditCardInfoEntity>

    /**
     * Observes all credit cards with credit cards associated to them from the credit card table.
     *
     * @return all credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM cashBackCreditCard")
    fun observeAll(): Flow<List<CashBackCreditCardEntity>>

    /**
     * Select all credit cards from the credit card table.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM cashBackCreditCard")
    suspend fun getAllInfo(): List<CashBackCreditCardInfoEntity>

    /**
     * Select a credit card by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM cashBackCreditCard WHERE id = :id")
    suspend fun getInfoById(id: UUID): CashBackCreditCardInfoEntity?

    /**
     * Select a credit card with associated purchase returns by id.
     *
     * @param id the credit card id.
     * @return the credit card with associated purchase returns with id.
     */
    @Transaction
    @Query("SELECT * FROM cashBackCreditCard WHERE id = :id")
    suspend fun getById(id: UUID): CashBackCreditCardEntity?

    /**
     * Select all credit cards with credit cards associated to them from the credit card table.
     *
     * @return all credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM cashBackCreditCard")
    suspend fun getAll(): List<CashBackCreditCardEntity>

    /**
     * Insert or update a credit card in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCard the credit card to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(cashBackCreditCard: CashBackCreditCardInfoEntity)

    /**
     * Insert or update credit cards in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCards the credit cards to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(cashBackCreditCards: List<CashBackCreditCardInfoEntity>)

    /**
     * Delete a credit card by id.
     *
     * @param id id of the credit card
     * @return the number of credit card deleted. This should always be 1.
     */
    @Query("DELETE FROM cashBackCreditCard WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    /**
     * Delete all credit cards.
     */
    @Query("DELETE FROM cashBackCreditCard")
    suspend fun deleteAll()
}
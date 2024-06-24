package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PointBackCreditCardDao {
    /**
     * Observes list of credit card info.
     *
     * @return all cash back credit cards' info.
     */
    @Query("SELECT * FROM pointBackCreditCard")
    fun observeAllInfo(): Flow<List<PointBackCreditCardInfoEntity>>

    /**
     * Observes a single credit card's info.
     *
     * @param id the credit card id.
     * @return the credit card info with id.
     */
    @Query("SELECT * FROM pointBackCreditCard WHERE id = :id")
    fun observeInfoById(id: UUID): Flow<PointBackCreditCardInfoEntity>

    /**
     * Observes all credit cards with purchase returns associated to them from the credit card table.
     *
     * @return all credit cards with purchase returns associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCreditCard")
    fun observeAll(): Flow<List<PointBackCreditCardEntity>>

    /**
     * Select all credit cards' info from the credit card table.
     *
     * @return all credit cards' info.
     */
    @Query("SELECT * FROM pointBackCreditCard")
    suspend fun getAllInfo(): List<PointBackCreditCardInfoEntity>

    /**
     * Select a credit card's info by id.
     *
     * @param id the credit card id.
     * @return the credit card's info with id.
     */
    @Query("SELECT * FROM pointBackCreditCard WHERE id = :id")
    suspend fun getInfoById(id: UUID): PointBackCreditCardInfoEntity?

    /**
     * Select a credit card with associated purchase returns by id.
     *
     * @param id the credit card id.
     * @return the credit card with associated purchase returns with id.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCreditCard WHERE id = :id")
    suspend fun getById(id: UUID): PointBackCreditCardEntity?

    /**
     * Select all credit cards with purchase returns associated to them from the credit card table.
     *
     * @return all credit cards with purchase returns associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCreditCard")
    suspend fun getAll(): List<PointBackCreditCardEntity>

    /**
     * Insert or update a credit card's info in the database. If a credit card already exists, replace it.
     *
     * @param cardInfo the credit card's info to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(cardInfo: PointBackCreditCardInfoEntity)

    /**
     * Insert or update credit cards' info in the database. If a credit card already exists, replace it.
     *
     * @param cardInfoList the credit cards' info to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(cardInfoList: List<PointBackCreditCardInfoEntity>)

    /**
     * Update the point system id of a credit card
     *
     * @param id id of the credit card
     * @param pointSystemId id of the point system to be associated with the credit card
     */
    @Query("UPDATE pointBackCreditCard SET pointSystemId = :pointSystemId WHERE id = :id")
    suspend fun updatePointSystemId(id: UUID, pointSystemId: UUID)

    /**
     * Delete a credit card by id.
     *
     * @param id id of the credit card
     * @return the number of credit card deleted. This should always be 1.
     */
    @Query("DELETE FROM pointBackCreditCard WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    /**
     * Delete all credit cards.
     */
    @Query("DELETE FROM pointBackCreditCard")
    suspend fun deleteAll()
}
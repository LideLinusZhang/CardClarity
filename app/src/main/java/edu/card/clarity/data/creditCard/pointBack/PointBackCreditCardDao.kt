package edu.card.clarity.data.creditCard.pointBack

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

@Dao
interface PointBackCreditCardDao {
    /**
     * Observes list of systems.
     *
     * @return all cash back credit cards.
     */
    @Query("SELECT * FROM pointBackCreditCard")
    fun observeAll(): Flow<List<PointBackCreditCardEntity>>

    /**
     * Observes a single credit card.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM pointBackCreditCard WHERE id = :id")
    fun observeById(id: UUID): Flow<PointBackCreditCardEntity>

    /**
     * Observes all credit cards with credit cards associated to them from the credit card table.
     *
     * @return all credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCreditCard")
    fun observeCashBackCreditCardWithPurchaseReturn(): Flow<List<PointBackCreditCardWithPurchaseReturns>>

    /**
     * Select all credit cards from the credit card table.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM pointBackCreditCard")
    suspend fun getAll(): List<PointBackCreditCardEntity>

    /**
     * Select a credit card by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM pointBackCreditCard WHERE id = :id")
    suspend fun getById(id: UUID): PointBackCreditCardEntity?

    /**
     * Select all credit cards with credit cards associated to them from the credit card table.
     *
     * @return all credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointBackCreditCard")
    suspend fun getCashBackCreditCardWithPurchaseReturn(): List<PointBackCreditCardEntity>

    /**
     * Insert or update a credit card in the database. If a credit card already exists, replace it.
     *
     * @param pointBackCreditCard the credit card to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(pointBackCreditCard: PointBackCreditCardEntity)

    /**
     * Insert or update credit cards in the database. If a credit card already exists, replace it.
     *
     * @param pointBackCreditCard the credit cards to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(pointBackCreditCard: List<PointBackCreditCardEntity>)

    /**
     * Update the statement date of a credit card
     *
     * @param id id of the credit card
     * @param statementDate statement date to be updated
     */
    @Query("UPDATE pointBackCreditCard SET statementDate = :statementDate WHERE id = :id")
    suspend fun updateStatementDate(id: UUID, statementDate: Date)

    /**
     * Update the statement date of a credit card
     *
     * @param id id of the credit card
     * @param paymentDueDate payment due date to be updated
     */
    @Query("UPDATE pointBackCreditCard SET paymentDueDate = :paymentDueDate WHERE id = :id")
    suspend fun updatePaymentDueDate(id: UUID, paymentDueDate: Date)

    /**
     * Update the point system id of a credit card
     *
     * @param id id of the credit card
     * @param pointSystemId id of the point system to be associated with the credit card
     */
    @Query("UPDATE pointBackCreditCard SET pointSystemId = :pointSystemId WHERE id = :id")
    suspend fun updatePointSystemId(id: UUID, pointSystemId: UUID)

    /**
     * Update the name of a credit card
     *
     * @param id id of the task
     * @param name name to be updated
     */
    @Query("UPDATE pointBackCreditCard SET name = :name WHERE id = :id")
    suspend fun updateName(id: UUID, name: String)

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
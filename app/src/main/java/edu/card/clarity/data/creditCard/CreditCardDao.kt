package edu.card.clarity.data.creditCard

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import edu.card.clarity.enums.RewardType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CreditCardDao {
    /**
     * Observe list of all credit cards.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM creditCardInfo")
    fun observeAllInfo(): Flow<List<CreditCardInfoEntity>>

    /**
     * Observe list of all credit cards of a certain reward type.
     *
     * @return all credit cards of a certain reward type.
     */
    @Query("SELECT * FROM creditCardInfo WHERE rewardType = :type")
    fun observeAllInfoOf(type: RewardType): Flow<List<CreditCardInfoEntity>>

    /**
     * Observe a single credit card's info by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    fun observeInfoById(id: UUID): Flow<CreditCardInfoEntity>

    /**
     * Observe a single credit card's reward type by id.
     *
     * @param id the credit card id.
     * @return the credit card's reward type.
     */
    @Query("SELECT rewardType FROM creditCardInfo WHERE id = :id")
    fun observeRewardTypeById(id: UUID): Flow<RewardType>

    /**
     * Observe all credit cards.
     *
     * @return all credit cards with credit cards associated to them.
     */
    @Query("SELECT * FROM creditCardInfo")
    fun observeAll(): Flow<List<CreditCardInfoEntity>>

    /**
     * Observe all credit cards of a certain reward type.
     *
     * @return all credit cards of a certain reward type with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM creditCardInfo WHERE rewardType = :rewardType")
    fun observeAllOf(rewardType: RewardType): Flow<List<CreditCardEntity>>

    /**
     * Observe all predefined credit cards.
     *
     * @return all predefined credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT creditCardInfo.* FROM creditCardInfo " +
            "JOIN predefinedCreditCardId " +
            "ON creditCardInfo.id = predefinedCreditCardId.creditCardId")
    fun observeAllPredefined(): Flow<List<CreditCardEntity>>

    /**
     * Select all credit cards from the credit card table.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM creditCardInfo")
    suspend fun getAllInfo(): List<CreditCardInfoEntity>

    /**
     * Select all credit cards of a certain reward type.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM creditCardInfo WHERE rewardType = :rewardType")
    suspend fun getAllInfoOf(rewardType: RewardType): List<CreditCardInfoEntity>

    /**
     * Select a credit card by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    suspend fun getInfoById(id: UUID): CreditCardInfoEntity?

    /**
     * Select a single credit card's reward type by id.
     *
     * @param id the credit card id.
     * @return the credit card's reward type.
     */
    @Query("SELECT rewardType FROM creditCardInfo WHERE id = :id")
    suspend fun getRewardTypeById(id: UUID): RewardType?

    /**
     * Select all credit cards of a certain reward type.
     *
     * @return all such credit cards with associated rewards.
     */
    @Transaction
    @Query("SELECT * FROM creditCardInfo WHERE rewardType = :rewardType")
    suspend fun getAllOf(rewardType: RewardType): List<CreditCardEntity>

    /**
     * Select all predefined credit cards.
     *
     * @return all predefined credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT creditCardInfo.* FROM creditCardInfo " +
            "JOIN predefinedCreditCardId " +
            "ON creditCardInfo.id = predefinedCreditCardId.creditCardId")
    suspend fun getAllPredefined(): List<CreditCardEntity>

    /**
     * Select a credit card with associated purchase rewards by id.
     *
     * @param id the credit card id.
     * @return the credit card with associated purchase rewards with id.
     */
    @Transaction
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    suspend fun getById(id: UUID): CreditCardEntity?

    /**
     * Insert or update a credit card in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCard the credit card to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(cashBackCreditCard: CreditCardInfoEntity)

    /**
     * Insert or update credit cards in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCards the credit cards to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(cashBackCreditCards: List<CreditCardInfoEntity>)

    /**
     * Delete a credit card by id.
     *
     * @param id id of the credit card
     * @return the number of credit card deleted. This should always be 1.
     */
    @Query("DELETE FROM creditCardInfo WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    /**
     * Delete all credit cards of a certain reward type.
     */
    @Query("DELETE FROM creditCardInfo WHERE rewardType = :rewardType")
    suspend fun deleteAllOf(rewardType: RewardType)
}
package edu.card.clarity.data.creditCard

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import edu.card.clarity.data.creditCard.predefined.PredefinedCreditCard
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCard
import edu.card.clarity.data.creditCard.userAdded.UserAddedCreditCardInfo
import edu.card.clarity.domain.creditCard.PointBackCreditCard
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
    @Query("SELECT * FROM userAddedCreditCard")
    fun observeAllInfo(): Flow<List<UserAddedCreditCardInfo>>

    /**
     * Observe list of all credit cards of a certain reward type.
     *
     * @return all credit cards of a certain reward type.
     */
    @Query("SELECT * FROM userAddedCreditCard WHERE rewardType = :rewardType")
    fun observeAllInfoOf(rewardType: RewardType): Flow<List<UserAddedCreditCardInfo>>

    /**
     * Observe a single credit card's info and its associated benefits by id.
     *
     * @param id the credit card id.
     * @return the credit card with the id and its associated benefits.
     */
    @Transaction
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    fun observeById(id: UUID): Flow<ICreditCard>

    /**
     * Observe a single credit card's info by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    fun observeInfoById(id: UUID): Flow<CreditCardInfo>

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
    @Transaction
    @Query("SELECT * FROM userAddedCreditCard")
    fun observeAll(): Flow<List<UserAddedCreditCard>>

    /**
     * Observe all credit cards of a certain reward type.
     *
     * @return all credit cards of a certain reward type with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM userAddedCreditCard WHERE rewardType = :rewardType"
    )
    fun observeAllOf(rewardType: RewardType): Flow<List<UserAddedCreditCard>>

    /**
     * Observe all predefined credit cards of a certain reward type.
     *
     * @return all such predefined credit cards with credit cards associated to them .
     */
    @Transaction
    @Query("SELECT * FROM predefinedCreditCard WHERE rewardType = :rewardType")
    fun observeAllPredefinedOf(rewardType: RewardType): Flow<List<PredefinedCreditCard>>

    /**
     * Select all credit cards from the credit card table.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM userAddedCreditCard")
    suspend fun getAllInfo(): List<UserAddedCreditCardInfo>

    /**
     * Select all credit cards of a certain reward type.
     *
     * @return all credit cards.
     */
    @Query("SELECT * FROM userAddedCreditCard WHERE rewardType = :rewardType")
    suspend fun getAllInfoOf(rewardType: RewardType): List<UserAddedCreditCardInfo>

    /**
     * Select a credit card by id.
     *
     * @param id the credit card id.
     * @return the credit card with id.
     */
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    suspend fun getInfoById(id: UUID): CreditCardInfo?

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
    @Query("SELECT * FROM userAddedCreditCard WHERE rewardType = :rewardType")
    suspend fun getAllOf(rewardType: RewardType): List<UserAddedCreditCard>

    /**
     * Select all predefined credit cards of a certain reward type.
     *
     * @return all such predefined credit cards with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM predefinedCreditCard WHERE rewardType = :rewardType")
    suspend fun getAllPredefinedOf(rewardType: RewardType): List<PredefinedCreditCard>

    /**
     * Select a credit card with associated purchase rewards by id.
     *
     * @param id the credit card id.
     * @return the credit card with associated purchase rewards with id.
     */
    @Transaction
    @Query("SELECT * FROM creditCardInfo WHERE id = :id")
    suspend fun getById(id: UUID): UserAddedCreditCard?

    /**
     * Insert or update a credit card in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCard the credit card to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(cashBackCreditCard: CreditCardInfo)

    /**
     * Insert or update credit cards in the database. If a credit card already exists, replace it.
     *
     * @param cashBackCreditCards the credit cards to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(cashBackCreditCards: List<CreditCardInfo>)

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
    @Query("DELETE FROM creditCardInfo " +
            "WHERE rewardType = :rewardType " +
            "AND id NOT IN (SELECT * FROM predefinedCreditCardId)")
    suspend fun deleteAllOf(rewardType: RewardType)
}
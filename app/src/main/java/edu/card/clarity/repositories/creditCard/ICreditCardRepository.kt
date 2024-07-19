package edu.card.clarity.repositories.creditCard

import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.ICreditCard
import edu.card.clarity.enums.PurchaseType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ICreditCardRepository {
    suspend fun createCreditCard(info: CreditCardInfo): UUID

    suspend fun addPurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun updatePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun removePurchaseReward(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    )

    suspend fun updateCreditCardInfo(info: CreditCardInfo)

    /**
     * Get a credit card's information and its associated purchase rewards by its ID.
     */
    suspend fun getCreditCard(id: UUID): ICreditCard?
    /**
     * Get only a credit card's information by its ID.
     */
    suspend fun getCreditCardInfo(id: UUID): CreditCardInfo?

    /**
     * Get all credit cards' information and their associated purchase rewards.
     */
    suspend fun getAllCreditCards(): List<ICreditCard>
    /**
     * Get all predefined credit cards' information and their associated purchase rewards.
     * An [ICreditCard] object representing a predefined credit card does
     * not have an ID associated to it.
     */
    suspend fun getAllPredefinedCreditCards(): List<ICreditCard>
    /**
     * Get information of all credit cards but without their associated purchase rewards.
     */
    suspend fun getAllCreditCardInfo(): List<CreditCardInfo>

    /**
     * Get a credit card's information and its associated purchase rewards by its ID as a stream.
     */
    fun getCreditCardStream(id: UUID): Flow<ICreditCard>
    /**
     * Get all credit cards' information and their associated purchase rewards as a stream.
     */
    fun getAllCreditCardsStream(): Flow<List<ICreditCard>>
    /**
     * Get all predefined credit cards' information and their associated
     * purchase rewards as a stream.
     * An [ICreditCard] object representing a predefined credit card does
     * not have an ID associated to it.
     */
    fun getAllPredefinedCreditCardsStream(): Flow<List<ICreditCard>>
    /**
     * Get information of all credit cards but without their associated
     * purchase rewards as a stream.
     */
    fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>>

    suspend fun deleteCreditCard(id: UUID)
    suspend fun deleteAllCreditCards()
}
package edu.card.clarity.repositories

import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseType
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.ICreditCard
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ICreditCardRepository {
    suspend fun createCreditCard(info: CreditCardInfo, pointSystem: PointSystem): UUID

    suspend fun addPurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun updatePurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        factor: Float
    )

    suspend fun removePurchaseReturn(creditCardId: UUID, purchaseTypes: List<PurchaseType>)

    suspend fun updateCreditCardInfo(id: UUID, info: CreditCardInfo)

    suspend fun getCreditCard(id: UUID): ICreditCard?
    suspend fun getCreditCardInfo(id: UUID): CreditCardInfo?

    suspend fun getAllCreditCards(): List<ICreditCard>
    suspend fun getAllCreditCardInfo(): List<CreditCardInfo>

    fun getAllCreditCardsStream(): Flow<List<ICreditCard>>
    fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>>

    suspend fun deleteCreditCard(id: UUID)
    suspend fun deleteAllCreditCards()
}
package edu.card.clarity.repositories

import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardDao
import edu.card.clarity.data.creditCard.pointBack.PointBackCreditCardInfoEntity
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnDao
import edu.card.clarity.data.purchaseReturn.multiplier.MultiplierPurchaseReturnEntity
import edu.card.clarity.dependencyInjection.DefaultDispatcher
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseType
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.domain.creditCard.PointBackCreditCard
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointBackCreditCardRepository @Inject constructor(
    private val creditCardDataSource: PointBackCreditCardDao,
    private val purchaseReturnDataSource: MultiplierPurchaseReturnDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ICreditCardRepository {
    override suspend fun createCreditCard(info: CreditCardInfo, pointSystem: PointSystem): UUID {
        val id = withContext(dispatcher) {
            UUID.randomUUID()
        }

        val cardInfoEntity = PointBackCreditCardInfoEntity(
            id,
            info.name,
            info.statementDate,
            info.paymentDueDate,
            pointSystem.id
        )

        creditCardDataSource.upsert(cardInfoEntity)

        return id
    }

    override suspend fun addPurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") multiplier: Float
    ) {
        require(multiplier > 1.0) { "Multiplier must be greater than 1" }

        withContext(dispatcher) {
            for (purchaseType in purchaseTypes) {
                purchaseReturnDataSource.upsert(
                    MultiplierPurchaseReturnEntity(
                        creditCardId = creditCardId,
                        purchaseType = purchaseType,
                        multiplier = multiplier
                    )
                )
            }
        }
    }

    override suspend fun updatePurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") multiplier: Float
    ) {
        addPurchaseReturn(creditCardId, purchaseTypes, multiplier)
    }

    override suspend fun removePurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>
    ) {
        withContext(dispatcher) {
            for (purchaseType in purchaseTypes) {
                purchaseReturnDataSource.delete(creditCardId, purchaseType)
            }
        }
    }

    override suspend fun updateCreditCardInfo(id: UUID, info: CreditCardInfo) {
        val cardInfoEntity = creditCardDataSource.getInfoById(id)?.copy(
            name = info.name,
            statementDate = info.statementDate,
            paymentDueDate = info.paymentDueDate
        ) ?: throw IllegalArgumentException("Credit card with ID $id not found.")

        creditCardDataSource.upsert(cardInfoEntity)
    }

    suspend fun updateCreditCardPointSystem(id: UUID, pointSystem: PointSystem) {
        if (creditCardDataSource.getInfoById(id) === null) {
            throw IllegalArgumentException("Credit card with ID $id not found.")
        }

        creditCardDataSource.updatePointSystemId(id, pointSystem.id)
    }

    override suspend fun getCreditCard(id: UUID): PointBackCreditCard? {
        return creditCardDataSource.getById(id)?.toDomainModel()
    }

    override suspend fun getCreditCardInfo(id: UUID): CreditCardInfo? {
        return creditCardDataSource.getInfoById(id)?.toDomainModel()
    }

    override suspend fun getAllCreditCards(): List<PointBackCreditCard> {
        return withContext(dispatcher) {
            creditCardDataSource.getAll().toDomainModel()
        }
    }

    override suspend fun getAllCreditCardInfo(): List<CreditCardInfo> {
        return withContext(dispatcher) {
            creditCardDataSource.getAllInfo().toDomainModel()
        }
    }

    override fun getAllCreditCardsStream(): Flow<List<PointBackCreditCard>> {
        return creditCardDataSource.observeAll().map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    override fun getAllCreditCardInfoStream(): Flow<List<CreditCardInfo>> {
        return creditCardDataSource.observeAllInfo().map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    override suspend fun deleteAllCreditCards() {
        creditCardDataSource.deleteAll()
    }

    override suspend fun deleteCreditCard(id: UUID) {
        creditCardDataSource.deleteById(id)
    }
}
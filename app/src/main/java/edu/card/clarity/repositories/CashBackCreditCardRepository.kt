package edu.card.clarity.repositories

import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardDao
import edu.card.clarity.data.creditCard.cashBack.CashBackCreditCardInfoEntity
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnDao
import edu.card.clarity.data.purchaseReturn.percentage.PercentagePurchaseReturnEntity
import edu.card.clarity.dependencyInjection.ApplicationScope
import edu.card.clarity.dependencyInjection.DefaultDispatcher
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.PurchaseType
import edu.card.clarity.domain.creditCard.CashBackCreditCard
import edu.card.clarity.domain.creditCard.CreditCardInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashBackCreditCardRepository @Inject constructor(
    private val creditCardDataSource: CashBackCreditCardDao,
    private val purchaseReturnDataSource: PercentagePurchaseReturnDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : ICreditCardRepository {
    override suspend fun createCreditCard(info: CreditCardInfo, pointSystem: PointSystem): UUID {
        val id = withContext(dispatcher) {
            UUID.randomUUID()
        }

        val cardInfoEntity = CashBackCreditCardInfoEntity(
            id,
            info.name,
            info.statementDate,
            info.paymentDueDate,
        )

        creditCardDataSource.upsert(cardInfoEntity)

        return id
    }

    override suspend fun addPurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") percentage: Float
    ) {
        require(percentage > 0.0 && percentage <= 1.0) { "Percentage be between 0 and 1 in float" }

        withContext(dispatcher) {
            for (purchaseType in purchaseTypes) {
                purchaseReturnDataSource.upsert(
                    PercentagePurchaseReturnEntity(
                        creditCardId = creditCardId,
                        purchaseType = purchaseType,
                        percentage = percentage
                    )
                )
            }
        }
    }

    override suspend fun updatePurchaseReturn(
        creditCardId: UUID,
        purchaseTypes: List<PurchaseType>,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") percentage: Float
    ) {
        addPurchaseReturn(creditCardId, purchaseTypes, percentage)
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

    override suspend fun getCreditCard(id: UUID): CashBackCreditCard? {
        return creditCardDataSource.getById(id)?.toDomainModel()
    }

    override suspend fun getCreditCardInfo(id: UUID): CreditCardInfo? {
        return creditCardDataSource.getInfoById(id)?.toDomainModel()
    }

    override suspend fun getAllCreditCards(): List<CashBackCreditCard> {
        return withContext(dispatcher) {
            creditCardDataSource.getAll().toDomainModel()
        }
    }

    override suspend fun getAllCreditCardInfo(): List<CreditCardInfo> {
        return withContext(dispatcher) {
            creditCardDataSource.getAllInfo().toDomainModel()
        }
    }

    override fun getAllCreditCardsStream(): Flow<List<CashBackCreditCard>> {
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
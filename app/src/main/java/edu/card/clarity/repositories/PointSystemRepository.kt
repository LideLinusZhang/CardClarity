package edu.card.clarity.repositories

import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.dependencyInjection.DefaultDispatcher
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.creditCard.CreditCardInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointSystemRepository @Inject constructor(
    private val dataSource: PointSystemDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) {
    suspend fun createPointSystem(name: String, conversionRate: Float): UUID {
        val id = withContext(dispatcher) {
            UUID.randomUUID()
        }

        val entity = PointSystemEntity(
            id = id,
            name = name,
            pointToCashConversionRate = conversionRate
        )

        dataSource.upsert(entity)

        return id
    }

    suspend fun getPointSystem(id: UUID): PointSystem? {
        return dataSource.getById(id)?.toDomainModel()
    }

    suspend fun getAllPointSystems(): List<PointSystem> {
        return dataSource.getAll().toDomainModel()
    }

    fun getAllPointSystemsStream(): Flow<List<PointSystem>> {
        return dataSource.observeAll().map {
            withContext(dispatcher) {
                it.toDomainModel()
            }
        }
    }

    suspend fun getCreditCardsUsingPointSystem(pointSystemId: UUID): List<CreditCardInfo> {
        return dataSource.getPointSystemWithCreditCards(pointSystemId)?.creditCards?.toDomainModel()
            ?: throw IllegalArgumentException("Point system of ID $pointSystemId does not exist")
    }

    suspend fun updateName(id: UUID, name: String) {
        val updated = dataSource.getById(id)?.copy(
            name = name
        ) ?: throw IllegalArgumentException("Point system of ID $id does not exist")

        dataSource.upsert(updated)
    }

    suspend fun updateConversionRate(id: UUID, rate: Float) {
        val updated = dataSource.getById(id)?.copy(
            pointToCashConversionRate = rate
        ) ?: throw IllegalArgumentException("Point system of ID $id does not exist")

        dataSource.upsert(updated)
    }

    suspend fun removePointSystem(id: UUID) {
        dataSource.deleteById(id)
    }

    suspend fun removeAllPointSystems() {
        dataSource.deleteAll()
    }
}
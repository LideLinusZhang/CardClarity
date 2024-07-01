package edu.card.clarity.repositories

import edu.card.clarity.data.pointSystem.PointSystemDao
import edu.card.clarity.data.pointSystem.PointSystemEntity
import edu.card.clarity.dependencyInjection.annotations.DefaultDispatcher
import edu.card.clarity.domain.PointSystem
import edu.card.clarity.domain.creditCard.CreditCardInfo
import edu.card.clarity.repositories.utils.toDomainModel
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
    suspend fun addPointSystem(pointSystem: PointSystem): UUID {
        val id = withContext(dispatcher) {
            UUID.randomUUID()
        }

        val entity = PointSystemEntity(
            id,
            pointSystem.name,
            pointSystem.pointToCashConversionRate
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

    suspend fun updatePointSystem(pointSystem: PointSystem) {
        require(pointSystem.id != null)
        require(dataSource.exist(pointSystem.id))

        dataSource.upsert(
            PointSystemEntity(
                pointSystem.id,
                pointSystem.name,
                pointSystem.pointToCashConversionRate
            )
        )
    }

    suspend fun removePointSystem(id: UUID) {
        dataSource.deleteById(id)
    }

    suspend fun removeAllPointSystems() {
        dataSource.deleteAll()
    }
}
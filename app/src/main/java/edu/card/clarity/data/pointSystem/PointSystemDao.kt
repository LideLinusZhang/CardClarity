package edu.card.clarity.data.pointSystem

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PointSystemDao {
    /**
     * Observes list of systems.
     *
     * @return all point systems.
     */
    @Query("SELECT * FROM pointSystem")
    fun observeAll(): Flow<List<PointSystemEntity>>

    /**
     * Observes a single point system.
     *
     * @param id the point system id.
     * @return the point system with id.
     */
    @Query("SELECT * FROM pointSystem WHERE id = :id")
    fun observeById(id: UUID): Flow<PointSystemEntity>

    /**
     * Observes all point systems with credit cards associated to them from the point system table.
     *
     * @return all point systems with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointSystem")
    fun observePointSystemWithCreditCards(): Flow<List<PointSystemWithCreditCards>>

    /**
     * Select all point systems from the point system table.
     *
     * @return all point systems.
     */
    @Query("SELECT * FROM pointSystem")
    suspend fun getAll(): List<PointSystemEntity>

    /**
     * Select a point system by id.
     *
     * @param id the point system id.
     * @return the point system with id.
     */
    @Query("SELECT * FROM pointSystem WHERE id = :id")
    suspend fun getById(id: UUID): PointSystemEntity?

    /**
     * Select all point systems with credit cards associated to them from the point system table.
     *
     * @return all point systems with credit cards associated to them.
     */
    @Transaction
    @Query("SELECT * FROM pointSystem")
    suspend fun getPointSystemWithCreditCards(): List<PointSystemWithCreditCards>

    /**
     * Insert or update a point system in the database. If a point system already exists, replace it.
     *
     * @param pointSystem the point system to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(pointSystem: PointSystemEntity)

    /**
     * Insert or update point systems in the database. If a point system already exists, replace it.
     *
     * @param pointSystems the point systems to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(pointSystems: List<PointSystemEntity>)

    /**
     * Update the conversion rate of a point system
     *
     * @param id id of the task
     * @param conversionRate conversion rate to be updated
     */
    @Query("UPDATE pointSystem SET pointToCashConversionRate = :conversionRate WHERE id = :id")
    suspend fun updateConversionRate(id: UUID, conversionRate: Float)

    /**
     * Update the name of a point system
     *
     * @param id id of the task
     * @param name name to be updated
     */
    @Query("UPDATE pointSystem SET name = :name WHERE id = :id")
    suspend fun updateName(id: UUID, name: String)

    /**
     * Delete a point system by id.
     *
     * @param id id of the point system
     * @return the number of point system deleted. This should always be 1.
     */
    @Query("DELETE FROM pointSystem WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    /**
     * Delete all point systems.
     */
    @Query("DELETE FROM pointSystem")
    suspend fun deleteAll()
}
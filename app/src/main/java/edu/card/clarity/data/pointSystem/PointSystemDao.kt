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
    @Query("SELECT * FROM pointSystem WHERE id = :id")
    fun observePointSystemWithCreditCards(id: UUID): Flow<PointSystemWithCreditCards>

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
    @Query("SELECT * FROM pointSystem WHERE id = :id")
    suspend fun getPointSystemWithCreditCards(id: UUID): PointSystemWithCreditCards?

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
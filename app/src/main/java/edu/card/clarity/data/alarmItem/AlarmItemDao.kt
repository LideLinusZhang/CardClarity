package edu.card.clarity.data.alarmItem
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.UUID

@Dao
interface AlarmItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmItem: AlarmItem): Long

    @Query("SELECT * FROM alarmItems WHERE creditCardId = :creditCardId")
    suspend fun getAlarmsForCreditCard(creditCardId: UUID): List<AlarmItem>

    @Query("DELETE FROM alarmItems WHERE id = :id")
    suspend fun deleteById(id: UUID): Int

    @Query("SELECT * FROM alarmItems")
    suspend fun getAllAlarms(): List<AlarmItem>
}
package ca.tetervak.donutdata3.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DonutDao {
    @Query("SELECT * FROM donuts")
    fun getAllDonutEntitiesFlow(): Flow<List<DonutEntity>>

    @Query("SELECT * FROM donuts WHERE id = :id")
    suspend fun getDonutEntityById(id: Long): DonutEntity

    @Insert
    suspend fun insertDonutEntity(donut: DonutEntity): Long

    @Delete
    suspend fun deleteDonutEntity(donut: DonutEntity)

    @Query("DELETE FROM donuts WHERE id=:id")
    suspend fun deleteDonutEntityById(id: Long)

    @Query("DELETE FROM donuts")
    suspend fun deleteAllDonutEntities()

    @Update
    suspend fun updateDonutEntity(donut: DonutEntity)
}

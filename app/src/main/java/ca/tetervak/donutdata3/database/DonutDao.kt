package ca.tetervak.donutdata3.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DonutDao {
    @Query("SELECT * FROM donuts")
    fun getAll(): LiveData<List<DonutEntity>>

    @Query("SELECT * FROM donuts WHERE id = :id")
    suspend fun get(id: Long): DonutEntity

    @Insert
    suspend fun insert(donut: DonutEntity): Long

    @Delete
    suspend fun delete(donut: DonutEntity)

    @Query("DELETE FROM donuts WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM donuts")
    suspend fun deleteAll()

    @Update
    suspend fun update(donut: DonutEntity)
}

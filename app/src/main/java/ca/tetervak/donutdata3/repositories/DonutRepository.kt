package ca.tetervak.donutdata3.repositories

import androidx.lifecycle.LiveData
import ca.tetervak.donutdata3.domain.Donut

interface DonutRepository {
    fun getAll(): LiveData<List<Donut>>
    suspend fun get(id: String): Donut
    suspend fun insert(donut: Donut): Long
    suspend fun delete(donut: Donut)
    suspend fun delete(id: String)
    suspend fun update(donut: Donut)
    suspend fun deleteAll()
}
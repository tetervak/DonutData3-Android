package ca.tetervak.donutdata3.repositories

import ca.tetervak.donutdata3.domain.Donut
import kotlinx.coroutines.flow.Flow

interface DonutRepository {
    fun getAll(): Flow<List<Donut>>
    suspend fun get(id: String): Donut
    suspend fun insert(donut: Donut): Long
    suspend fun delete(donut: Donut)
    suspend fun delete(id: String)
    suspend fun update(donut: Donut)
    suspend fun deleteAll()
}
package ca.tetervak.donutdata3.repositories

import ca.tetervak.donutdata3.domain.Donut
import kotlinx.coroutines.flow.Flow

interface DonutDataRepository {
    fun getAllDonutsFlow(): Flow<List<Donut>>
    suspend fun getDonutById(id: String): Donut
    suspend fun insertDonut(donut: Donut): String
    suspend fun deleteDonut(donut: Donut)
    suspend fun deleteDonutById(id: String)
    suspend fun updateDonut(donut: Donut)
    suspend fun deleteAllDonuts()
}
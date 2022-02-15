package ca.tetervak.donutdata3.repositories

interface DonutImageNameRepository {

    suspend fun getAllDonutImageNames(): List<String>?
}
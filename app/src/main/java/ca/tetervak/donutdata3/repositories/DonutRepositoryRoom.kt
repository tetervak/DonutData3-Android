package ca.tetervak.donutdata3.repositories

import android.util.Log
import ca.tetervak.donutdata3.database.DonutDao
import ca.tetervak.donutdata3.database.DonutEntity
import ca.tetervak.donutdata3.domain.Donut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DonutRepositoryRoom @Inject constructor(private val donutDao: DonutDao)
    : DonutRepository {

    companion object{
        private const val TAG = "DonutRepositoryRoom"
    }

    init{
        Log.d(TAG, "init: the DonutRepositoryRoom object is created")
    }

    override fun getAllDonutsFlow(): Flow<List<Donut>> =
        donutDao.getAllDonutEntitiesFlow()
            .map { list -> list.map { it.asDonut() } }
            .flowOn(Dispatchers.IO)


    override suspend fun getDonutById(id: String): Donut {
        return donutDao.getDonutEntityById(id.toLong()).asDonut()
    }

    override suspend fun insertDonut(donut: Donut): String {
        return donutDao.insertDonutEntity(donut.asEntity()).toString()
    }

    override suspend fun deleteDonut(donut: Donut) {
        if (donut.id != null) {
            donutDao.deleteDonutEntity(donut.asEntity())
        }
    }

    override suspend fun deleteDonutById(id: String) {
        donutDao.deleteDonutEntityById(id.toLong())
    }

    override suspend fun updateDonut(donut: Donut) {
        donutDao.updateDonutEntity(donut.asEntity())
    }

    override suspend fun deleteAllDonuts() {
        donutDao.deleteAllDonutEntities()
    }
}

fun DonutEntity.asDonut(): Donut {
    return Donut(
        id = id.toString(),
        name = name,
        description = description,
        rating = rating,
        lowFat = lowFat,
        brand = brand,
        imageFile = imageFile,
        date = date
    )
}

fun Donut.asEntity(): DonutEntity {
    return DonutEntity(
        id = id?.toLong() ?: 0L,
        name = name,
        description = description,
        rating = rating,
        lowFat = lowFat,
        brand = brand,
        imageFile = imageFile,
        date = date
    )
}
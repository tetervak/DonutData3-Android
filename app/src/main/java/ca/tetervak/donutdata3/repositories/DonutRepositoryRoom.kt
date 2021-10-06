package ca.tetervak.donutdata3.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.tetervak.donutdata3.database.DonutDao
import ca.tetervak.donutdata3.database.DonutEntity
import ca.tetervak.donutdata3.domain.Donut
import javax.inject.Inject

class DonutRepositoryRoom @Inject constructor(private val donutDao: DonutDao)
    : DonutRepository {

    companion object{
        private const val TAG = "DonutRepositoryRoom"
    }

    init{
        Log.d(TAG, "init: the DonutRepositoryRoom object is created")
    }

    override fun getAll(): LiveData<List<Donut>> {
        return Transformations.map(donutDao.getAll()) { list -> list.map { it.asDonut() }}
    }

    override suspend fun get(id: String): Donut {
        return donutDao.get(id.toLong()).asDonut()
    }

    override suspend fun insert(donut: Donut): Long {
        return donutDao.insert(donut.asEntity())
    }

    override suspend fun delete(donut: Donut) {
        if (donut.id != null) {
            donutDao.delete(donut.id.toLong())
        }
    }

    override suspend fun delete(id: String) {
        donutDao.delete(id.toLong())
    }

    override suspend fun update(donut: Donut) {
        donutDao.update(donut.asEntity())
    }

    override suspend fun deleteAll() {
        donutDao.deleteAll()
    }
}

fun DonutEntity.asDonut(): Donut {
    return Donut(id.toString(), name, description, rating, lowFat)
}

fun Donut.asEntity(): DonutEntity {
    return DonutEntity(id?.toLong() ?: 0L, name, description, rating, lowFat)
}
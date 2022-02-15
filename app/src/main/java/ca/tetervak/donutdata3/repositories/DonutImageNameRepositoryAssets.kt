package ca.tetervak.donutdata3.repositories

import android.app.Application
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

import javax.inject.Inject

class DonutImageNameRepositoryAssets @Inject constructor(
    private val application: Application
) : DonutImageNameRepository {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getAllDonutImageNames(): List<String>? {
        return withContext(Dispatchers.IO) {
            application.assets.list("images/donuts/")?.asList()
        }
    }
}
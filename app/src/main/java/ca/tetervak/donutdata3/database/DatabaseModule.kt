package ca.tetervak.donutdata3.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val TAG = "DataBaseModule"

    @Provides
    fun provideDonutDao(database: DonutDatabase): DonutDao {
        Log.d(TAG, "provideDonutDao: the DonutDao object is returned")
        return database.donutDao()
    }

    @Singleton
    @Provides
    fun provideDonutDatabase(@ApplicationContext context: Context): DonutDatabase {
        Log.d(TAG, "provideDonutDatabase: the database object is created")
        return Room.databaseBuilder(
            context,
            DonutDatabase::class.java,
            "donut_database"
        ).build()
    }
}
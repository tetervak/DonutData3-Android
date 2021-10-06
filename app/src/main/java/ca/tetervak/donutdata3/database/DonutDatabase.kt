package ca.tetervak.donutdata3.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DonutEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DonutDatabase : RoomDatabase() {

    abstract fun donutDao(): DonutDao
}

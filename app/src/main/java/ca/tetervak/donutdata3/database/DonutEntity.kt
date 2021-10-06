package ca.tetervak.donutdata3.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class holds the data that we are tracking for each donut: its name, a description, and
 * a rating.
 */
@Entity(tableName = "donuts")
data class DonutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "rating")
    val rating: Float,

    @ColumnInfo(name = "low_fat")
    val lowFat: Boolean = false
)

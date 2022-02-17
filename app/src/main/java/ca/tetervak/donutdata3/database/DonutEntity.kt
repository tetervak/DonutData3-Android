package ca.tetervak.donutdata3.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ca.tetervak.donutdata3.model.Brand
import java.util.*

@Entity(tableName = "donuts")
data class DonutEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val description: String = "",
    val rating: Float,

    @ColumnInfo(name = "low_fat")
    val lowFat: Boolean,

    val brand: Brand,

    @ColumnInfo(name = "image_file")
    val imageFile: String,

    @ColumnInfo(name = "time_stamp")
    val date: Date = Date()
)

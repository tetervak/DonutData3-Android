package ca.tetervak.donutdata3.database

import androidx.room.TypeConverter
import ca.tetervak.donutdata3.model.Brand
import java.util.*

class Converters {

    @TypeConverter
    fun fromLongToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromDateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromBrandToInt(brand: Brand): Int{
        return brand.ordinal
    }

    @TypeConverter
    fun fromIntToBrand(code: Int): Brand {
        return Brand.values()[code]
    }
}
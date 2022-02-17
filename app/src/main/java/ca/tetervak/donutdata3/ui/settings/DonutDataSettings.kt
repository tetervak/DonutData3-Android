package ca.tetervak.donutdata3.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import ca.tetervak.donutdata3.model.Brand
import ca.tetervak.donutdata3.domain.SortBy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonutDataSettings @Inject constructor(@ApplicationContext context: Context){
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    val defaultDescription: String
        get() = preferences.getString("default_description", "")!!

    val defaultBrand: Brand
        get() {
            val index = preferences.getString("default_brand_index", "0")!!.toInt()
            return Brand.values()[index]
        }

    val defaultLowFat: Boolean
        get() = preferences.getBoolean("default_low_fat", false)

    var confirmDelete: Boolean
        get() = preferences.getBoolean("confirm_delete", true)
        set(pref) = preferences.edit().putBoolean("confirm_delete", pref).apply()

    var confirmClear: Boolean
        get() = preferences.getBoolean("confirm_clear", true)
        set(pref) = preferences.edit().putBoolean("confirm_clear", pref).apply()

    var sortBy: SortBy
        get() {
            val index = preferences.getString("sorting_index", "3")!!.toInt()
            return SortBy.values()[index]
        }
        set(pref) {
            preferences.edit {
                putString("sorting_index", pref.ordinal.toString())
            }
        }
}

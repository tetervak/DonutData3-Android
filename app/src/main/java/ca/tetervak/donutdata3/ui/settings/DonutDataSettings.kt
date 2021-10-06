package ca.tetervak.donutdata3.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonutDataSettings @Inject constructor(@ApplicationContext context: Context){
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    var confirmDelete: Boolean
        get() = preferences.getBoolean("confirm_delete", true)
        set(pref) = preferences.edit().putBoolean("confirm_delete", pref).apply()

    var confirmClear: Boolean
        get() = preferences.getBoolean("confirm_clear", true)
        set(pref) = preferences.edit().putBoolean("confirm_clear", pref).apply()

}

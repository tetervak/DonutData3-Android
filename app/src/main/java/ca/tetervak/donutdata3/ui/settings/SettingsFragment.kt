package ca.tetervak.donutdata3.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ca.tetervak.donutdata3.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
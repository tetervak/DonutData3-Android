package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ca.tetervak.donutdata3.R

class AboutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.app_name)
            .setMessage(R.string.author)
            .setPositiveButton(android.R.string.ok, null)
            .create()

}
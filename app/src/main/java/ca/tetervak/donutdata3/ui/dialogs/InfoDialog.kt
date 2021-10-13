package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class InfoDialog : DialogFragment() {

    companion object {
        const val TAG = "InfoDialog"
        private const val TITLE = "title"
        private const val MESSAGE = "message"

        private fun newInstance(title: String, message: String): InfoDialog {
            Log.d(TAG, "newInstance: called")
            return InfoDialog().apply {
                arguments = bundleOf( TITLE to title, MESSAGE to message)
            }
        }

        fun showInfoDialog(
            fragmentManager: FragmentManager,
            title: String,
            message: String
        ) {
            Log.d(TAG, "showInfoDialog: called")
            newInstance(title, message).show(fragmentManager, TAG)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: called")
        return AlertDialog.Builder(requireContext())
            .setTitle(requireArguments().getString(TITLE))
            .setMessage(requireArguments().getString(MESSAGE))
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }
}
package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/*
    This dialog can be used with or without Navigation Component.

    If used with Navigation component, pass the following 2 required arguments
        title: String
        message: String

    Otherwise, if used without Navigation Component,
    use showInfoDialog function to open the dialog.
*/
class InfoDialog : DialogFragment() {

    companion object {
        const val TAG = "InfoDialog"
        private const val TITLE = "title"
        private const val MESSAGE = "message"

        fun showInfoDialog(
            activity: AppCompatActivity,
            title: String,
            message: String
        ) {
            showInfoDialog(
                activity.supportFragmentManager,
                title,
                message)
        }

        fun showInfoDialog(
            fragment: Fragment,
            title: String,
            message: String
        ) {
            showInfoDialog(
                fragment.parentFragmentManager,
                title,
                message)
        }

        private fun showInfoDialog(
            fragmentManager: FragmentManager,
            title: String,
            message: String
        ) {
            newInstance(title, message).show(fragmentManager, TAG)
        }

        private fun newInstance(title: String, message: String): InfoDialog {
            return InfoDialog().apply {
                arguments = bundleOf( TITLE to title, MESSAGE to message)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(requireArguments().getString(TITLE))
            .setMessage(requireArguments().getString(MESSAGE))
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }
}
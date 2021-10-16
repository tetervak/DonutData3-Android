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

fun AppCompatActivity.showInfoDialog(title: String, message: String) =
    InfoDialog.showInfoDialog(
        supportFragmentManager,
        title,
        message
    )

fun Fragment.showInfoDialog(title: String, message: String) =
    InfoDialog.showInfoDialog(
        parentFragmentManager,
        title,
        message
    )


class InfoDialog : DialogFragment() {

    companion object {
        private const val TAG = "InfoDialog"
        private const val TITLE = "title"
        private const val MESSAGE = "message"

        fun showInfoDialog(
            fragmentManager: FragmentManager,
            title: String,
            message: String
        ) = newInstance(title, message).show(fragmentManager, TAG)


        private fun newInstance(title: String, message: String) =
            InfoDialog().apply {
                arguments = bundleOf(TITLE to title, MESSAGE to message)
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        AlertDialog.Builder(requireContext())
            .setTitle(requireArguments().getString(TITLE))
            .setMessage(requireArguments().getString(MESSAGE))
            .setPositiveButton(android.R.string.ok, null)
            .create()
}
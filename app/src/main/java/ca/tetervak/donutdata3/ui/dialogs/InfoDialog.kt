package ca.tetervak.donutdata3.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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
            showInfoDialog(activity.supportFragmentManager, title, message)
        }

        fun showInfoDialog(
            fragment: Fragment,
            title: String,
            message: String
        ) {
            showInfoDialog(fragment.childFragmentManager, title, message)
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
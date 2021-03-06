package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import ca.tetervak.donutdata3.databinding.ConfirmationDialogBinding
import java.io.Serializable

/*
    This dialog can be used with or without Navigation Component.

    If used with Navigation component, pass the following 4 required arguments
        requestKey: String
        title: String
        message: String
        itemId: String?

    Otherwise, if used without Navigation Component,
    use showConfirmationDialog function to open the dialog.

    To get the result use setConfirmationResultListener function when
    used with or without Navigation Component
*/

fun AppCompatActivity.showConfirmationDialog(
    requestKey: String,
    title: String,
    message: String,
    itemId: String? = null
) = ConfirmationDialog.showConfirmationDialog(
    supportFragmentManager,
    requestKey,
    title,
    message,
    itemId
)

fun Fragment.showConfirmationDialog(
    requestKey: String,
    title: String,
    message: String,
    itemId: String? = null
) = ConfirmationDialog.showConfirmationDialog(
    parentFragmentManager,
    requestKey,
    title,
    message,
    itemId
)

fun AppCompatActivity.setConfirmationResultListener(
    requestKey: String,
    onResult: (ConfirmationDialog.ConfirmationResult) -> Unit
) = ConfirmationDialog.setConfirmationResultListener(
    supportFragmentManager,
    this,
    requestKey,
    onResult
)

fun Fragment.setConfirmationResultListener(
    requestKey: String,
    onResult: (ConfirmationDialog.ConfirmationResult) -> Unit
) = ConfirmationDialog.setConfirmationResultListener(
    parentFragmentManager,
    viewLifecycleOwner,
    requestKey,
    onResult
)

class ConfirmationDialog : DialogFragment() {

    data class ConfirmationResult(
        val itemId: String?,
        val doNotAskAgain: Boolean = false
    ) : Serializable

    companion object {
        private const val TAG = "ConfirmationDialog"
        private const val REQUEST_KEY = "requestKey"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val ITEM_ID = "itemId"
        private const val CONFIRMATION_RESULT = "confirmationResult"

        private fun newInstance(
            requestKey: String,
            title: String,
            message: String,
            itemId: String?
        ) = ConfirmationDialog().apply {
            arguments = bundleOf(
                REQUEST_KEY to requestKey,
                TITLE to title,
                MESSAGE to message,
                ITEM_ID to itemId
            )
        }


        fun showConfirmationDialog(
            fragmentManager: FragmentManager,
            requestKey: String,
            title: String,
            message: String,
            itemId: String?
        ) = newInstance(requestKey, title, message, itemId).show(fragmentManager, TAG)

        fun setConfirmationResultListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            onResult: (ConfirmationResult) -> Unit
        ) = fragmentManager.setFragmentResultListener(
            requestKey,
            lifecycleOwner
        ) { _, bundle ->
            onResult(bundle.getSerializable(CONFIRMATION_RESULT) as ConfirmationResult)
            fragmentManager.clearFragmentResult(requestKey)
        }
    }

    private lateinit var requestKey: String
    private var itemId: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        requireArguments().apply {
            requestKey = getString(REQUEST_KEY)!!
            itemId = getString(ITEM_ID)
        }

        val inflater = requireActivity().layoutInflater
        val binding = ConfirmationDialogBinding.inflate(inflater, null, false)
        binding.confirmationDialogMessage.text = requireArguments().getString(MESSAGE)

        return AlertDialog.Builder(requireActivity()).apply {
            setTitle(requireArguments().getString(TITLE))
            setView(binding.root)
            setPositiveButton(android.R.string.ok) { _, _ ->
                confirmed(binding.doNotAskAgainCheckBox.isChecked)
            }
            setNegativeButton(android.R.string.cancel, null)
        }.create()
    }

    private fun confirmed(doNotAskAgain: Boolean) =
        parentFragmentManager.setFragmentResult(
            requestKey,
            bundleOf(CONFIRMATION_RESULT to ConfirmationResult(itemId, doNotAskAgain))
        )
}
package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.ConfirmationDialogBinding
import java.io.Serializable

class ConfirmationDialog : DialogFragment() {

    data class ConfirmationResult(
        val requestCode: Int,
        val itemId: String?,
        val doNotAskAgain: Boolean = false
    ) : Serializable

    companion object {
        private const val TAG = "ConfirmationDialog"
        const val CONFIRMATION_RESULT = "confirmation_result"

        fun setResultListener(
            fragment: Fragment,
            fragmentId: Int,
            onResult: (ConfirmationResult?) -> Unit
        ) {
            val navController = fragment.findNavController()
            val navBackStackEntry = navController.getBackStackEntry(fragmentId)
            val handle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME
                    && handle.contains(CONFIRMATION_RESULT)
                ) {
                    val result: ConfirmationResult? = handle.get(CONFIRMATION_RESULT);
                    onResult(result)
                    handle.remove<ConfirmationResult>(CONFIRMATION_RESULT)
                    Log.d(TAG, "setResultListener: onResult is called")
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)
            fragment.viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    Log.d(TAG, "setResultListener: the fragment view is destroyed")
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    private val safeArgs: ConfirmationDialogArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        navController = findNavController()

        val inflater = requireActivity().layoutInflater
        val binding = ConfirmationDialogBinding.inflate(inflater, null, false)
        binding.confirmationDialogMessage.text = safeArgs.message

        return AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.app_name)
            setView(binding.root)
            setPositiveButton(android.R.string.ok) { _, _ ->
                confirmed(binding.doNotAskAgainCheckBox.isChecked)
            }
            setNegativeButton(android.R.string.cancel, null)
        }.create()
    }

    private fun confirmed(doNotAskAgain: Boolean) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(
            CONFIRMATION_RESULT, ConfirmationResult(safeArgs.requestCode, safeArgs.itemId, doNotAskAgain)
        )
    }

}
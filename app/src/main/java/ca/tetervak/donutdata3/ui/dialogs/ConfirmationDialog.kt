package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.ConfirmationDialogBinding
import java.io.Serializable

fun Fragment.setConfirmationResultListener(
    backFragmentId: Int,
    requestKey: String,
    onResult: (ConfirmationResult) -> Unit
) = setDialogResultListener(
    backFragmentId,
    requestKey,
    onResult
)


data class ConfirmationResult(
    val itemId: String?,
    val doNotAskAgain: Boolean = false
) : Serializable

class ConfirmationDialog : DialogFragment() {

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
            safeArgs.requestKey, ConfirmationResult(safeArgs.itemId, doNotAskAgain)
        )
    }

}
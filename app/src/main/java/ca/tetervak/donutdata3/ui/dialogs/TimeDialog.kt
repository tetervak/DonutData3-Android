package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.util.*

fun Fragment.setTimeResultListener(
    backFragmentId: Int,
    requestKey: String,
    onResult: (Date) -> Unit
) {
    setDialogResultListener(
        backFragmentId,
        requestKey,
        onResult
    )
}

class TimeDialog : DialogFragment() {

    private val safeArgs: TimeDialogArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        navController = findNavController()

        val calendar: Calendar = Calendar.getInstance().apply {
            time = safeArgs.date
        }

        return TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                setTimeResult(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
    }

    private fun setTimeResult(date: Date) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(safeArgs.requestKey, date)
    }
}
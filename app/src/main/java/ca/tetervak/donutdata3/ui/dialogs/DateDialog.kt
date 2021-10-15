package ca.tetervak.donutdata3.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.util.*

fun Fragment.setDateResultListener(
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

class DateDialog : DialogFragment() {

    private val safeArgs: DateDialogArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        navController = findNavController()

        val calendar: Calendar = Calendar.getInstance().apply {
            time = safeArgs.date
        }

        return DatePickerDialog(requireActivity()).apply {

            updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            setOnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)
                setDateResult(calendar.time)
            }
        }
    }

    private fun setDateResult(date: Date) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(safeArgs.requestKey, date)
    }

}
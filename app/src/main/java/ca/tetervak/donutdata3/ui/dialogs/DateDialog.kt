package ca.tetervak.donutdata3.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.io.Serializable
import java.util.*

class DateDialog : DialogFragment() {

    data class DateResult(
        val requestCode: Int,
        val date: Date
    ) : Serializable

    companion object {
        private const val DATE_RESULT = "dateResult"

        fun setResultListener(
            fragment: Fragment,
            fragmentId: Int,
            onResult: (DateResult?) -> Unit
        ) {
            val navController = fragment.findNavController()
            val navBackStackEntry = navController.getBackStackEntry(fragmentId)
            val handle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME
                    && handle.contains(DATE_RESULT)
                ) {
                    val result: DateResult? = handle.get(DATE_RESULT)
                    onResult(result)
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)
            fragment.viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    private val safeArgs: DateDialogArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        navController = findNavController()

        val calendar: Calendar = Calendar.getInstance().apply{
            time = safeArgs.date
        }

        return DatePickerDialog(requireActivity()).apply{

            updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))

            setOnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)
                setDateResult(calendar.time)
            }
        }
    }

    private fun setDateResult(date: Date) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(DATE_RESULT, DateResult(safeArgs.requestCode, date))
    }

}
package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.util.*


class TimeDialog : DialogFragment() {

    companion object {

        fun setResultListener(
            fragment: Fragment,
            fragmentId: Int,
            requestKey: String,
            onResult: (Date) -> Unit
        ) {
            val navController = fragment.findNavController()
            val navBackStackEntry = navController.getBackStackEntry(fragmentId)
            val handle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME
                    && handle.contains(requestKey)
                ) {
                    val date: Date? = handle.get(requestKey)
                    if(date is Date){
                        onResult(date)
                    }
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
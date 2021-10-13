package ca.tetervak.donutdata3.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import java.util.*

class TimeDialog : DialogFragment() {

    companion object {
        private const val TAG = "TimeDialog"
        private const val REQUEST_KEY = "requestKey"
        private const val DATE = "date"

        fun showTimeDialog(
            activity: AppCompatActivity,
            requestKey: String,
            date: Date
        ) {
            showTimeDialog(activity.supportFragmentManager, requestKey, date)
        }

        fun showTimeDialog(
            parentFragment: Fragment,
            requestKey: String,
            date: Date
        ) {
            showTimeDialog(parentFragment.childFragmentManager, requestKey, date)
        }

        private fun showTimeDialog(
            fragmentManager: FragmentManager,
            requestKey: String,
            date: Date
        ) {
            newInstance(requestKey, date).show(fragmentManager, TAG)
        }

        private fun newInstance(requestKey: String, date: Date): TimeDialog {
            return TimeDialog().apply {
                arguments = bundleOf(REQUEST_KEY to requestKey, DATE to date)
            }
        }

        fun setTimeResultListener(
            parentFragment: Fragment,
            requestKey: String,
            onResult: (Date) -> Unit
        ) {
            setTimeResultListener(
                parentFragment.childFragmentManager,
                parentFragment.viewLifecycleOwner,
                requestKey,
                onResult
            )
        }

        fun setTimeResultListener(
            activity: AppCompatActivity,
            requestKey: String,
            onResult: (Date) -> Unit
        ) {
            setTimeResultListener(
                activity.supportFragmentManager,
                activity,
                requestKey,
                onResult
            )
        }

        private fun setTimeResultListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            onResult: (Date) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(
                requestKey,
                lifecycleOwner
            ) { _, bundle ->
                onResult(bundle.getSerializable(DATE) as Date)
            }
        }
    }

    private lateinit var requestKey: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        requestKey = requireArguments().getString(REQUEST_KEY)!!

        val calendar: Calendar = Calendar.getInstance().apply {
            time = requireArguments().getSerializable(DATE) as Date
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
        parentFragmentManager.setFragmentResult(requestKey, bundleOf(DATE to date))
    }

}
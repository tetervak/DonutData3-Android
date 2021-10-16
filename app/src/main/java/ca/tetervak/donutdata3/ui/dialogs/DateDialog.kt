package ca.tetervak.donutdata3.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import java.util.*

/*
    This dialog can be used with or without Navigation Component.

    If used with Navigation component, pass the following 2 required arguments
        requestKey: String
        date: Date
    The date argument is allowed because it is Serializable

    Otherwise, if used without Navigation Component,
    use showDateDialog function to open the dialog.

    To get the result use setDateResultListener function when
    used with or without Navigation Component
*/

fun AppCompatActivity.showDateDialog(requestKey: String, date: Date) =
    DateDialog.showDateDialog(
        supportFragmentManager,
        requestKey,
        date
    )


fun Fragment.showDateDialog(requestKey: String, date: Date) =
    DateDialog.showDateDialog(
        parentFragmentManager,
        requestKey,
        date
    )


fun AppCompatActivity.setDateResultListener(requestKey: String, onResult: (Date) -> Unit) =
    DateDialog.setDateResultListener(
        supportFragmentManager,
        this,
        requestKey,
        onResult
    )


fun Fragment.setDateResultListener(requestKey: String, onResult: (Date) -> Unit) =
    DateDialog.setDateResultListener(
        parentFragmentManager,
        viewLifecycleOwner,
        requestKey,
        onResult
    )


class DateDialog : DialogFragment() {

    companion object {
        private const val TAG = "DateDialog"
        private const val REQUEST_KEY = "requestKey"
        private const val DATE = "date"

        fun showDateDialog(
            fragmentManager: FragmentManager,
            requestKey: String,
            date: Date
        ) = newInstance(requestKey, date).show(fragmentManager, TAG)


        fun setDateResultListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            onResult: (Date) -> Unit
        ) = fragmentManager.setFragmentResultListener(
            requestKey,
            lifecycleOwner
        ) { _, bundle ->
            onResult(bundle.getSerializable(DATE) as Date)
        }


        private fun newInstance(requestKey: String, date: Date) =
            DateDialog().apply {
                arguments = bundleOf(REQUEST_KEY to requestKey, DATE to date)
            }

    }

    private lateinit var requestKey: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        requestKey = requireArguments().getString(REQUEST_KEY)!!

        val calendar = Calendar.getInstance().apply {
            time = requireArguments().getSerializable(DATE) as Date
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

    private fun setDateResult(date: Date) =
        parentFragmentManager.setFragmentResult(requestKey, bundleOf(DATE to date))

}
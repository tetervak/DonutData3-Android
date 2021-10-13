package ca.tetervak.donutdata3.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.*

class DateDialog : DialogFragment() {

    companion object {
        private const val TAG = "DateDialog"
        private const val REQUEST_KEY = "requestKey"
        private const val DATE = "date"

        private fun newInstance(requestKey: String, date: Date): DateDialog {
            return DateDialog().apply {
                arguments = bundleOf(REQUEST_KEY to requestKey, DATE to date)
            }
        }

        fun showDateDialog(
            parentFragment: Fragment,
            requestKey: String,
            date: Date
        ) {
            newInstance(requestKey, date).show(parentFragment.childFragmentManager, TAG)
        }

        fun setDateResultLister(
            parentFragment: Fragment,
            requestKey: String,
            onResult: (Date) -> Unit
        ) {
            parentFragment.childFragmentManager.setFragmentResultListener(
                requestKey,
                parentFragment.viewLifecycleOwner
            ) { _, bundle ->
                onResult(bundle.getSerializable(DATE) as Date)
            }
        }

    }

    private lateinit var requestKey: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        requestKey = requireArguments().getString(REQUEST_KEY)!!

        val calendar = Calendar.getInstance().apply {
            time = requireArguments().getSerializable(DATE) as Date
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
        parentFragmentManager.setFragmentResult(requestKey, bundleOf(DATE to date))
    }

}
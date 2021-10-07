package ca.tetervak.donutdata3.ui.newdonut

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.NewDonutFragmentBinding
import ca.tetervak.donutdata3.domain.Brand
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.ui.dialogs.DateDialog
import ca.tetervak.donutdata3.ui.dialogs.TimeDialog
import ca.tetervak.donutdata3.ui.selectimage.SelectImageFragment.Companion.FILE_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NewDonutFragment : Fragment() {

    companion object{
        private const val DATE_REQUEST: Int = 1
        private const val TIME_REQUEST: Int = 1
    }

    private var _binding: NewDonutFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private var donutImage: String = "cinnamon_sugar.png"
    private var date: Date = Date()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NewDonutFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()

        binding.fileName = donutImage
        binding.date = date

        binding.changeImageButton.setOnClickListener {
            navController.navigate(
                NewDonutFragmentDirections.actionNewDonutToSelectImage(donutImage)
            )
        }

        binding.saveButton.setOnClickListener {
            mainViewModel.save(
                Donut(
                    id = null,
                    name = binding.name.text.toString(),
                    description =  binding.description.text.toString(),
                    rating = binding.ratingBar.rating,
                    lowFat = binding.lowFatCheckBox.isChecked,
                    brand = Brand.values()[binding.brandSpinner.selectedItemPosition],
                    imageFile = donutImage,
                    date = date
                )
            )
            showList()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            showList()
        }

        // get donut file name from SelectImageFragment
        navController
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(FILE_NAME)
            ?.observe(viewLifecycleOwner) { fileName ->
                donutImage = fileName
                binding.fileName = donutImage
            }

        binding.dateLink.setOnClickListener {
            navController.navigate(
                NewDonutFragmentDirections.actionNewDonutToDateDialog(DATE_REQUEST, date)
            )
        }

        // get date from DateDialog
        DateDialog.setResultListener(this, R.id.nav_new_donut){ result->
            if(result?.requestCode == DATE_REQUEST){
                date = result.date
                binding.date = date
            }
        }

        binding.timeLink.setOnClickListener {
            navController.navigate(
                NewDonutFragmentDirections.actionNewDonutToTimeDialog(TIME_REQUEST, date)
            )
        }

        // get time from TimeDialog
        TimeDialog.setResultListener(this, R.id.nav_new_donut){ result->
            if(result?.requestCode == TIME_REQUEST){
                date = result.date
                binding.date = date
            }
        }

        return binding.root
    }

    private fun showList() {
        navController.navigate(
            NewDonutFragmentDirections.actionGlobalDonutList()
        )
    }

    private fun hideKeyboard(){
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
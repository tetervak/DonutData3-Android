package ca.tetervak.donutdata3.ui.newdonut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ca.tetervak.donutdata3.ui.settings.DonutDataSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NewDonutFragment : Fragment() {

    companion object{
        private const val DATE_REQUEST: Int = 1
        private const val TIME_REQUEST: Int = 1
        private const val DATE = "date"
    }

    private var _binding: NewDonutFragmentBinding? = null
    private val binding: NewDonutFragmentBinding
        get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private var donutImage: String = "cinnamon_sugar.png"
    private var date: Date = Date()

    @Inject
    lateinit var settings: DonutDataSettings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NewDonutFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()

        if (savedInstanceState is Bundle) {
            date = savedInstanceState.getSerializable(DATE) as Date
        }

        binding.fileName = donutImage
        binding.date = date
        binding.brandSpinner.setSelection(settings.defaultBrand.ordinal)

        binding.card.setOnClickListener {
            onChangeImage()
        }
        binding.changeImageButton.setOnClickListener {
            onChangeImage()
        }

        binding.saveDonutFab.setOnClickListener {
            onSave()
        }

        binding.saveButton.setOnClickListener {
            onSave()
        }

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

    private fun onSave() {
        if (binding.name.text.toString().isNotBlank()) {
            mainViewModel.save(
                Donut(
                    id = null,
                    name = binding.name.text.toString(),
                    description = binding.description.text.toString(),
                    rating = binding.ratingBar.rating,
                    lowFat = binding.lowFatCheckBox.isChecked,
                    brand = Brand.values()[binding.brandSpinner.selectedItemPosition],
                    imageFile = donutImage,
                    date = date
                )
            )
            showList()
        } else {
            binding.name.error = getString(R.string.cannot_be_blank)
        }
    }

    private fun onChangeImage() {
        navController.navigate(
            NewDonutFragmentDirections.actionNewDonutToSelectImage(donutImage)
        )
    }

    private fun showList() {
        navController.navigate(
            NewDonutFragmentDirections.actionGlobalDonutList()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(DATE, date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
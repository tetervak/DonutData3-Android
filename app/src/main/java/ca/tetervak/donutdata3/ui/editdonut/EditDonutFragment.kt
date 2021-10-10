package ca.tetervak.donutdata3.ui.editdonut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.EditDonutFragmentBinding
import ca.tetervak.donutdata3.domain.Brand
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.ui.binding.bindDate
import ca.tetervak.donutdata3.ui.binding.bindDonutImage
import ca.tetervak.donutdata3.ui.binding.bindTime
import ca.tetervak.donutdata3.ui.dialogs.DateDialog
import ca.tetervak.donutdata3.ui.dialogs.TimeDialog
import ca.tetervak.donutdata3.ui.selectimage.SelectImageFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditDonutFragment : Fragment() {

    companion object{
        private const val DATE_REQUEST: Int = 1
        private const val TIME_REQUEST: Int = 1
        private const val DONUT_IMAGE = "donutImage"
        private const val DATE = "date"
        private const val IS_DONUT_LOADED = "isDonutLoaded"
    }

    private val safeArgs: EditDonutFragmentArgs by navArgs()
    private val editDonutViewModel: EditDonutViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private var donutImage: String = "cinnamon_sugar.png"
    private var date: Date = Date()

    private var isDonutLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditDonutFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()

        if (savedInstanceState is Bundle) {
            donutImage = savedInstanceState.getString(DONUT_IMAGE,"cinnamon_sugar.png")
            date = savedInstanceState.getSerializable(DATE) as Date
            isDonutLoaded = savedInstanceState.getBoolean(IS_DONUT_LOADED, false)
        }

        bindDonutImage(binding.donutImage, donutImage)

        bindDate(binding.dateLink, date)
        bindTime(binding.timeLink, date)

        if(!isDonutLoaded) {
            editDonutViewModel.donut.observe(viewLifecycleOwner) { donut ->
                binding.name.setText(donut.name)
                binding.description.setText(donut.description)
                binding.ratingBar.rating = donut.rating
                binding.lowFatCheckBox.isChecked = donut.lowFat
                binding.brandSpinner.setSelection(donut.brand.ordinal)

                donutImage = donut.imageFile
                bindDonutImage(binding.donutImage, donutImage)

                date = donut.date
                bindDate(binding.dateLink, date)
                bindTime(binding.timeLink, date)

                isDonutLoaded = true
            }
        }

        binding.card.setOnClickListener {
            changeImage()
        }
        binding.changeImageButton.setOnClickListener {
            changeImage()
        }

        binding.saveButton.setOnClickListener {
            if(binding.name.text.toString().isNotBlank()) {
                mainViewModel.save(
                    Donut(
                        id = safeArgs.donutId,
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
            } else {
                binding.name.error = getString(R.string.cannot_be_blank)
            }
        }

        binding.cancelButton.setOnClickListener {
            showList()
        }

        // get donut file name from SelectImageFragment
        navController
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(SelectImageFragment.FILE_NAME)
            ?.observe(viewLifecycleOwner) { fileName ->
                donutImage = fileName
                bindDonutImage(binding.donutImage, donutImage)
            }

        binding.dateLink.setOnClickListener {
            navController.navigate(
                EditDonutFragmentDirections.actionEditDonutToDateDialog(DATE_REQUEST, date)
            )
        }

        // get date from DateDialog
        DateDialog.setResultListener(this, R.id.nav_edit_donut){ result->
            if(result?.requestCode == DATE_REQUEST){
                date = result.date
                bindDate(binding.dateLink, date)
            }
        }

        binding.timeLink.setOnClickListener {
            navController.navigate(
                EditDonutFragmentDirections.actionEditDonutToTimeDialog(TIME_REQUEST, date)
            )
        }

        // get time from TimeDialog
        TimeDialog.setResultListener(this, R.id.nav_edit_donut){ result->
            if(result?.requestCode == TIME_REQUEST){
                date = result.date
                bindTime(binding.timeLink, date)
            }
        }

        return binding.root
    }

    private fun changeImage() {
        navController.navigate(
            EditDonutFragmentDirections.actionEditDonutToSelectImage(donutImage)
        )
    }

    private fun showList() {
        findNavController().navigate(
            EditDonutFragmentDirections.actionGlobalDonutList()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DONUT_IMAGE, donutImage)
        outState.putSerializable(DATE, date)
        outState.putSerializable(IS_DONUT_LOADED, isDonutLoaded)
    }
}
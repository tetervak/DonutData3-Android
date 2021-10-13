package ca.tetervak.donutdata3.ui.editdonut

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
import ca.tetervak.donutdata3.ui.dialogs.ConfirmationDialog
import ca.tetervak.donutdata3.ui.dialogs.DateDialog.Companion.setDateResultListener
import ca.tetervak.donutdata3.ui.dialogs.DateDialog.Companion.showDateDialog
import ca.tetervak.donutdata3.ui.dialogs.TimeDialog
import ca.tetervak.donutdata3.ui.dialogs.TimeDialog.Companion.setTimeResultListener
import ca.tetervak.donutdata3.ui.dialogs.TimeDialog.Companion.showTimeDialog
import ca.tetervak.donutdata3.ui.selectimage.SelectImageFragment
import ca.tetervak.donutdata3.ui.settings.DonutDataSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditDonutFragment : Fragment() {

    companion object{
        private const val TAG = "EditDonutFragment"
        private const val CONFIRM_DELETE_ITEM = "confirmDeleteItem"
        private const val GET_DATE = "getDate"
        private const val GET_TIME = "getTime"
        private const val DONUT_IMAGE = "donutImage"
        private const val DATE = "date"
        private const val IS_DONUT_LOADED = "isDonutLoaded"
    }

    private var _binding: EditDonutFragmentBinding? = null
    private val binding: EditDonutFragmentBinding
        get() = _binding!!

    private val safeArgs: EditDonutFragmentArgs by navArgs()
    private val editDonutViewModel: EditDonutViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private var donutImage: String = "cinnamon_sugar.png"
    private var date: Date = Date()

    private var isDonutLoaded: Boolean = false

    @Inject
    lateinit var settings: DonutDataSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EditDonutFragmentBinding.inflate(inflater, container, false)
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
            onChangeImage()
        }
        binding.changeImageButton.setOnClickListener {
            onChangeImage()
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
            ?.getLiveData<String>(SelectImageFragment.FILE_NAME)
            ?.observe(viewLifecycleOwner) { fileName ->
                donutImage = fileName
                bindDonutImage(binding.donutImage, donutImage)
            }

        binding.dateLink.setOnClickListener {
            showDateDialog(this, GET_DATE, date)
        }

        // get date from DateDialog
        setDateResultListener(this, GET_DATE) {
            date = it
            bindDate(binding.dateLink, date)
        }

        binding.timeLink.setOnClickListener {
            showTimeDialog(this, GET_TIME, date)
        }

        // get time from TimeDialog
        setTimeResultListener(this, GET_TIME) {
            date = it
            bindTime(binding.timeLink, date)
        }

        ConfirmationDialog.setResultListener(
            this, R.id.nav_edit_donut, CONFIRM_DELETE_ITEM) { result ->
            mainViewModel.delete(result.itemId!!)
            if(result.doNotAskAgain){
                settings.confirmDelete = false
            }
            showList()
        }

        return binding.root
    }

    private fun onSave() {
        if (binding.name.text.toString().isNotBlank()) {
            mainViewModel.save(
                Donut(
                    id = safeArgs.donutId,
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                onDelete()
                true
            }
            R.id.action_save -> {
                onSave()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onDelete() {
        if (settings.confirmDelete) {
            val action = EditDonutFragmentDirections.actionEditDonutToConfirmation(
                getString(R.string.confirm_delete_message),
                CONFIRM_DELETE_ITEM,
                safeArgs.donutId
            )
            navController.navigate(action)
        } else {
            mainViewModel.delete(safeArgs.donutId)
            showList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package ca.tetervak.donutdata3.ui.editdonut

import android.os.Bundle
import android.util.Log
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
import ca.tetervak.donutdata3.ui.dialogs.*
import ca.tetervak.donutdata3.ui.selectimage.setSelectImageResultListener
import ca.tetervak.donutdata3.ui.settings.DonutDataSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditDonutFragment : Fragment() {

    companion object {
        private const val TAG = "EditDonutFragment"
        private const val DEFAULT_IMAGE = "cinnamon_sugar.png"
        private const val CONFIRM_DELETE_ITEM = "confirmDeleteItem"
        private const val GET_IMAGE = "getImage"
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

    private var donutImage: String = DEFAULT_IMAGE
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
            donutImage = savedInstanceState.getString(DONUT_IMAGE, DEFAULT_IMAGE)
            date = savedInstanceState.getSerializable(DATE) as Date
            isDonutLoaded = savedInstanceState.getBoolean(IS_DONUT_LOADED, false)
        }

        bindDonutImage(binding.donutImage, donutImage)

        bindDate(binding.dateLink, date)
        bindTime(binding.timeLink, date)

        if (!isDonutLoaded) {
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
        setSelectImageResultListener(requestKey = GET_IMAGE) { fileName ->
            Log.d(TAG, "onCreateView: the file name is received")
            donutImage = fileName
            bindDonutImage(binding.donutImage, donutImage)
        }

        binding.dateLink.setOnClickListener {
            showDateDialog(requestKey = GET_DATE, date = date)
        }

        // get date from DateDialog
        setDateResultListener(requestKey = GET_DATE) {
            Log.d(TAG, "onCreateView: the date is received")
            date = it
            bindDate(binding.dateLink, date)
        }

        binding.timeLink.setOnClickListener {
            showTimeDialog(requestKey = GET_TIME, date = date)
        }

        // get time from TimeDialog
        setTimeResultListener(requestKey = GET_TIME) {
            Log.d(TAG, "onCreateView: the time is received")
            date = it
            bindTime(binding.timeLink, date)
        }

        setConfirmationResultListener(requestKey = CONFIRM_DELETE_ITEM) { result ->
            mainViewModel.delete(result.itemId!!)
            if (result.doNotAskAgain) {
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
            EditDonutFragmentDirections.actionEditDonutToSelectImage(GET_IMAGE, donutImage)
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
            showConfirmationDialog(
                requestKey = CONFIRM_DELETE_ITEM,
                title = getString(R.string.app_name),
                message = getString(R.string.confirm_delete_message),
                itemId = safeArgs.donutId
            )
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
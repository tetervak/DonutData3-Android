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
import ca.tetervak.donutdata3.databinding.NewDonutFragmentBinding
import ca.tetervak.donutdata3.domain.Brand
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.ui.selectimage.SelectImageFragment.Companion.FILE_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewDonutFragment : Fragment() {


    private var _binding: NewDonutFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private var donutImage: String = "cinnamon_sugar.png"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NewDonutFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()

        binding.fileName = donutImage

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
                    imageFile = donutImage
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
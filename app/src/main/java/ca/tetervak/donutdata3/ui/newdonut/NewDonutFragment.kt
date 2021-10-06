package ca.tetervak.donutdata3.ui.newdonut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.databinding.NewDonutFragmentBinding
import ca.tetervak.donutdata3.domain.Donut
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewDonutFragment : Fragment() {

    private var _binding: NewDonutFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NewDonutFragmentBinding.inflate(inflater, container, false)

        binding.saveButton.setOnClickListener {
            mainViewModel.save(
                Donut(
                    id = null,
                    name = binding.name.text.toString(),
                    description =  binding.description.text.toString(),
                    rating = binding.ratingBar.rating
                )
            )
            showList()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            showList()
        }

        return binding.root
    }

    private fun showList() {
        findNavController().navigate(
            NewDonutFragmentDirections.actionGlobalDonutList()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
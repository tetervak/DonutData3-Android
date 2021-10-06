package ca.tetervak.donutdata3.ui.donutentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.databinding.DonutEntryFragmentBinding
import ca.tetervak.donutdata3.domain.Donut
import dagger.hilt.android.AndroidEntryPoint

/**
 * This dialog allows the user to enter information about a donut, either creating a new
 * entry or updating an existing one.
 */
@AndroidEntryPoint
class DonutEntryFragment : Fragment() {

    private val safeArgs: DonutEntryFragmentArgs by navArgs()
    private val donutEntryViewModel: DonutEntryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DonutEntryFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = donutEntryViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.saveButton.setOnClickListener {
            mainViewModel.save(
                Donut(
                    id = safeArgs.donutId,
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
                DonutEntryFragmentDirections.actionEntryToList()
        )
    }
}

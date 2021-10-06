package ca.tetervak.donutdata3.ui.editdonut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.databinding.EditDonutFragmentBinding
import ca.tetervak.donutdata3.domain.Donut
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDonutFragment : Fragment() {

    private val safeArgs: EditDonutFragmentArgs by navArgs()
    private val editDonutViewModel: EditDonutViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = EditDonutFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = editDonutViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.saveButton.setOnClickListener {
            mainViewModel.save(
                Donut(
                    id = safeArgs.donutId,
                    name = binding.name.text.toString(),
                    description = binding.description.text.toString(),
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
            EditDonutFragmentDirections.actionGlobalDonutList()
        )
    }

}
package ca.tetervak.donutdata3.ui.selectimage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.databinding.SelectImageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

fun Fragment.setSelectImageResultListener(
    requestKey: String,
    onResult: (String) -> Unit
){
    SelectImageFragment.setSelectImageResultListener(
        parentFragmentManager,
        viewLifecycleOwner,
        requestKey,
        onResult
    )
}

@AndroidEntryPoint
class SelectImageFragment : Fragment() {

    companion object{
        private const val TAG = "SelectImageFragment"
        private const val FILE_NAME = "fileName"

        fun setSelectImageResultListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            onResult: (String) -> Unit
        ){
            fragmentManager.setFragmentResultListener(requestKey, lifecycleOwner) { _, bundle ->
                onResult(bundle.getString(FILE_NAME)!!)
            }
        }
    }

    private val safeArgs: SelectImageFragmentArgs by navArgs()
    private val viewModel: SelectImageViewModel by viewModels()
    private lateinit var adapter: SelectImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SelectImageFragmentBinding.inflate(inflater, container, false)
        val navController = findNavController()

        adapter = SelectImageAdapter()
        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { itemUiStateList ->
                    adapter.submitList(itemUiStateList)
                }
            }
        }

        binding.cancelButton.setOnClickListener{
            navController.popBackStack()
        }

        binding.okButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                safeArgs.requestKey, bundleOf(FILE_NAME to viewModel.selectedFileName))
            navController.popBackStack()
        }

        return binding.root
    }

}
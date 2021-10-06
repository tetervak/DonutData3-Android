package ca.tetervak.donutdata3.ui.imagelist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ca.tetervak.donutdata3.databinding.ImageListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageListFragment : Fragment() {

    companion object{
        const val TAG = "ImageListFragment"
        const val SELECTED_ITEM_INDEX = "selectedItemIndex"
    }

    private val imageListViewModel: ImageListViewModel by viewModels()

    private lateinit var adapter: ImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ImageListFragmentBinding.inflate(inflater, container, false)

        adapter = ImageListAdapter{
            binding.okButton.isEnabled = true
        }

        binding.recyclerView.adapter = adapter
        binding.viewModel = imageListViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(savedInstanceState is Bundle){
            if(savedInstanceState.containsKey(SELECTED_ITEM_INDEX)){
                val index = savedInstanceState.getInt(SELECTED_ITEM_INDEX)
                adapter.selectedItemIndex = index
                binding.okButton.isEnabled = true
            }
        }

        binding.cancelButton.setOnClickListener{
            Log.d(TAG, "onCreateView: Cancel button is clicked")
        }

        binding.okButton.setOnClickListener {
            Log.d(TAG, "onCreateView: OK button is clicked")
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val index = adapter.selectedItemIndex
        if(index != null){
            outState.putInt(SELECTED_ITEM_INDEX, index)
        }
    }


}
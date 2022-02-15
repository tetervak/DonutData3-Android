package ca.tetervak.donutdata3.ui.selectimage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.databinding.SelectImageFragmentBinding
import ca.tetervak.donutdata3.ui.newdonut.NewDonutFragment
import dagger.hilt.android.AndroidEntryPoint

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
    private val selectImageViewModel: SelectImageViewModel by viewModels()
    private lateinit var adapter: SelectImageAdapter
    private lateinit var fileName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SelectImageFragmentBinding.inflate(inflater, container, false)
        val navController = findNavController()
        fileName = safeArgs.fileName

        if(savedInstanceState is Bundle){
            fileName = savedInstanceState.getString(FILE_NAME)!!
        }

        adapter = SelectImageAdapter{ name ->
            fileName = name
        }
        binding.recyclerView.adapter = adapter
        selectImageViewModel.imageList.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
            adapter.selectImage(fileName)
        }

        binding.cancelButton.setOnClickListener{
            navController.popBackStack()
        }

        binding.okButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                safeArgs.requestKey, bundleOf(FILE_NAME to fileName))
            navController.popBackStack()
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FILE_NAME, fileName)
    }

}
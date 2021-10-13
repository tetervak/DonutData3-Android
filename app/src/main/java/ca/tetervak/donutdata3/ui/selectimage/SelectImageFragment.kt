package ca.tetervak.donutdata3.ui.selectimage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.tetervak.donutdata3.databinding.SelectImageFragmentBinding
import ca.tetervak.donutdata3.ui.newdonut.NewDonutFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectImageFragment : Fragment() {

    companion object{
        private const val TAG = "SelectImageFragment"
        private const val FILE_NAME = "fileName"

        fun setSelectImageResultListener(
            backFragment: Fragment,
            requestKey: String,
            onResult: (String) -> Unit
        ){
            backFragment.findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<String>(requestKey)
                ?.observe(backFragment.viewLifecycleOwner) { fileName ->
                   onResult(fileName)
                }
        }
    }

    private val safeArgs: SelectImageFragmentArgs by navArgs()
    private val selectImageViewModel: SelectImageViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: SelectImageAdapter
    private lateinit var fileName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SelectImageFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()
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
            Log.d(TAG, "onCreateView: Cancel button is clicked")
            navController.popBackStack()
        }

        binding.okButton.setOnClickListener {
            Log.d(TAG, "onCreateView: OK button is clicked")
            navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set(safeArgs.requestKey, fileName)
            navController.popBackStack()
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FILE_NAME, fileName)
    }


}
package ca.tetervak.donutdata3.ui.donutlist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import ca.tetervak.donutdata3.MainViewModel
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.DonutListFragmentBinding
import ca.tetervak.donutdata3.ui.dialogs.ConfirmationDialog
import ca.tetervak.donutdata3.ui.settings.DonutDataSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DonutListFragment : Fragment() {

    companion object{
        private const val TAG = "DonutListFragment"
        private const val CONFIRM_CLEAR_ALL: Int = 1
        private const val CONFIRM_DELETE_ITEM: Int = 2
    }

    private val donutListViewModel: DonutListViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

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

        val binding = DonutListFragmentBinding.inflate(inflater, container, false)
        navController = findNavController()

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)
        val adapter = DonutListAdapter(
            onEdit = { donut ->
                navController.navigate(
                    DonutListFragmentDirections.actionDonutListToEditDonut(donut.id!!)
                )
            },
            onDelete = { donut ->
                if(settings.confirmDelete){
                    val action = DonutListFragmentDirections.actionDonutListToConfirmation(
                        getString(R.string.confirm_delete_message),
                        CONFIRM_DELETE_ITEM,
                        donut.id
                    )
                    navController.navigate(action)
                }else{
                    mainViewModel.delete(donut)
                }
            }
        )
        binding.recyclerView.adapter = adapter

        binding.viewModel = donutListViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.newDonutFab.setOnClickListener {
            navController.navigate(
                DonutListFragmentDirections.actionDonutListToNewDonut()
            )
        }

        ConfirmationDialog.setResultListener(this, R.id.nav_donut_list) {
            when (it?.requestCode) {
                CONFIRM_CLEAR_ALL -> {
                    Log.d(TAG, "onCreateView: clear all is confirmed")
                    donutListViewModel.deleteAll()
                    if(it.doNotAskAgain){
                        settings.confirmClear = false
                    }
                }
                CONFIRM_DELETE_ITEM -> {
                    Log.d(TAG, "onCreateView: delete item id=${it.itemId} is confirmed")
                    mainViewModel.delete(it.itemId!!)
                    if(it.doNotAskAgain){
                        settings.confirmDelete = false
                    }
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                if(settings.confirmClear){
                    val action = DonutListFragmentDirections.actionDonutListToConfirmation(
                        getString(R.string.confirm_clear_message),
                        CONFIRM_CLEAR_ALL,
                        null
                    )
                    navController.navigate(action)
                }else{
                    donutListViewModel.deleteAll()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

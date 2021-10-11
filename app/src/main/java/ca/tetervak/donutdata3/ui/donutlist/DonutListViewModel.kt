package ca.tetervak.donutdata3.ui.donutlist

import android.util.Log
import androidx.lifecycle.*
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.domain.SortBy
import ca.tetervak.donutdata3.domain.sort
import ca.tetervak.donutdata3.repositories.DonutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonutListViewModel @Inject constructor(
    private val repository: DonutRepository
) : ViewModel() {

    companion object{
        private const val TAG = "DonutListViewModel"
    }

    private val donuts: LiveData<List<Donut>> = repository.getAll()

    private val _sortBy = MutableLiveData(SortBy.SORT_BY_ID)
    fun setSorting(sortBy: SortBy){
        _sortBy.value = sortBy
    }

    private val _donutList = MediatorLiveData<List<Donut>>()
    val donutList: LiveData<List<Donut>> = _donutList

    init{
        _donutList.addSource(donuts){ list ->
            _donutList.value = sort(list, _sortBy.value!!)
        }
        _donutList.addSource(_sortBy){ sortBy ->
            val list = _donutList.value
            if(list is List<Donut>){
                _donutList.value = sort(list, sortBy)
            }
        }

        Log.d(TAG, "init: the DonutListViewModel object is created")
    }

    fun deleteAll() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
}

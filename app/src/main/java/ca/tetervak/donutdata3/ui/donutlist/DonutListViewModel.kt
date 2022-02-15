package ca.tetervak.donutdata3.ui.donutlist

import android.util.Log
import androidx.lifecycle.*
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.domain.SortBy
import ca.tetervak.donutdata3.domain.sort
import ca.tetervak.donutdata3.repositories.DonutDataRepository
import ca.tetervak.donutdata3.ui.settings.DonutDataSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonutListViewModel @Inject constructor(
    private val repository: DonutDataRepository,
    settings: DonutDataSettings
) : ViewModel() {

    companion object{
        private const val TAG = "DonutListViewModel"
    }

    private val _sortBy = MutableLiveData(settings.sortBy)
    fun setSorting(sortBy: SortBy){
        _sortBy.value = sortBy
    }

    private val donuts: LiveData<List<Donut>> = repository.getAllDonutsFlow().asLiveData()
    val donutList: LiveData<List<Donut>> =
        _sortBy.switchMap { sortBy ->
            donuts.map { list ->
                sort(list, sortBy)
            }
        }

    init{
        Log.d(TAG, "init: the DonutListViewModel object is created")
    }

    fun deleteAllDonuts() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllDonuts()
        }
}

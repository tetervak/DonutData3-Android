package ca.tetervak.donutdata3.ui.donutlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.tetervak.donutdata3.domain.Donut
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

    init{
        Log.d(TAG, "init: the DonutListViewModel object is created")
    }

    val donutList: LiveData<List<Donut>> = repository.getAll()

    fun deleteAll() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
}

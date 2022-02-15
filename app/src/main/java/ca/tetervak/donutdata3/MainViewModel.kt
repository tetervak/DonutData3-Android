package ca.tetervak.donutdata3

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.repositories.DonutDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DonutDataRepository
) : ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        Log.d(TAG, "init: the MainViewModel is created")
    }

    fun deleteDonut(donut: Donut) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDonut(donut)
        }

    fun deleteDonutById(id: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDonutById(id)
        }

    fun saveDonut(donut: Donut) =
        viewModelScope.launch(Dispatchers.IO) {
            if (donut.id == null) {
                repository.insertDonut(donut)
            } else {
                repository.updateDonut(donut)
            }
        }
}
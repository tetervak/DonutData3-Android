package ca.tetervak.donutdata3.ui.selectimage

import android.util.Log
import androidx.lifecycle.*
import ca.tetervak.donutdata3.repositories.DonutImageNameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectImageViewModel @Inject constructor(
    repository: DonutImageNameRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ImageListViewModel"
    }

    val imageList: LiveData<List<String>> = liveData {
        repository.getAllDonutImageNames()?.let { list ->
            emit(list)
        }
    }

    init {
        Log.d(TAG, "init: the ImageListViewModel is created")
    }
}
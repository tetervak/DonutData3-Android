package ca.tetervak.donutdata3.ui.editdonut

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ca.tetervak.donutdata3.domain.Donut
import ca.tetervak.donutdata3.repositories.DonutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditDonutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DonutRepository
) : ViewModel() {

    companion object{
        private const val TAG = "EditDonutViewModel"
    }

    init{
        Log.d(TAG, "init: the EditDonutViewModel is created")
    }

    private val donutId: String? = savedStateHandle["donutId"]

    val donut: LiveData<Donut> =
        liveData {
            emit(repository.get(donutId!!))
        }
}
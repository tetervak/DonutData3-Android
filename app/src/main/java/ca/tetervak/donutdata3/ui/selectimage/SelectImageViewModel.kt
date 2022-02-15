package ca.tetervak.donutdata3.ui.selectimage

import android.util.Log
import androidx.lifecycle.*
import ca.tetervak.donutdata3.repositories.DonutImageNameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectImageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: DonutImageNameRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ImageListViewModel"
    }

    var selectedFileName: String = savedStateHandle["fileName"]!!
    private set

    private val _uiState = MutableStateFlow(emptyList<ImageListItemUiState>())
    val uiState: StateFlow<List<ImageListItemUiState>> = _uiState.asStateFlow()

    init{
        Log.d(TAG, "init: the ImageListViewModel is created")
        viewModelScope.launch {
            repository.getAllDonutImageNames()?.let { fileNames ->
                _uiState.update {
                    fileNames.map { name ->
                        ImageListItemUiState(
                            fileName = name,
                            isSelected = (name == selectedFileName),
                            onSelect = {
                                selectImage(fileName = name)
                            }
                        )
                    }
                }
            }
        }
    }

    fun selectImage(fileName: String){
        selectedFileName = fileName
        _uiState.update { itemUiStateList ->
            itemUiStateList.map { itemUiState ->
                itemUiState.copy(isSelected = (itemUiState.fileName == fileName))
            }
        }
    }
}
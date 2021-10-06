package ca.tetervak.donutdata3.ui.imagelist

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(@ApplicationContext context: Context): ViewModel(){
    companion object{
        private const val TAG = "ImageListViewModel"
    }

    private val list : List<String>? = context.assets.list("images/donuts/")?.toList()
    val imageList: LiveData<List<String>> = MutableLiveData(list!!)

    init{
        Log.d(TAG, "init: the ImageListViewModel is created")
    }
}
package ca.tetervak.donutdata3.ui.dialogs

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import java.io.Serializable

fun <T: Serializable> Fragment.setDialogResultListener(
    backFragmentId: Int,
    requestKey: String,
    onResult: (T) -> Unit
){
    val backStackEntry = findNavController().getBackStackEntry(backFragmentId)
    val savedStateHandle = backStackEntry.savedStateHandle
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && savedStateHandle.contains(requestKey)
        ) {
            val value: T? = savedStateHandle.get(requestKey)
            if (value is T) {
                onResult(value)
                savedStateHandle.remove<T>(requestKey)
            }
        }
    }
    backStackEntry.lifecycle.addObserver(observer)
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            backStackEntry.lifecycle.removeObserver(observer)
        }
    })
}
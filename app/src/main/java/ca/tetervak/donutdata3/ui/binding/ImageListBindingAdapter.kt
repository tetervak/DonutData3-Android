package ca.tetervak.donutdata3.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.donutdata3.ui.imagelist.ImageListAdapter

@BindingAdapter("imageList")
fun bindImageList(recyclerView: RecyclerView, list: List<String>?){
    if(list is List<String>){
        val adapter = recyclerView.adapter as ImageListAdapter
        adapter.submitList(list)
    }
}
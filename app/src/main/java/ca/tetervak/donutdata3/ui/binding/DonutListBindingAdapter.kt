package ca.tetervak.donutdata3.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.donutdata3.model.Donut
import ca.tetervak.donutdata3.ui.donutlist.DonutListAdapter

@BindingAdapter("donutList")
fun bindDonutList(recyclerView: RecyclerView, list: List<Donut>?){
    if(list is List<Donut>){
        val adapter = recyclerView.adapter as DonutListAdapter
        adapter.submitList(list)
    }
}
package ca.tetervak.donutdata3.ui.imagelist

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.ImageListItemBinding

class ImageListAdapter(
    private var onSelect: (String) -> Unit
): RecyclerView.Adapter<ImageListAdapter.ViewHolder>(){

    private var list: List<String>? = null

    var selectedItemIndex: Int? = null
    set(index){
        if(index != field){
            val previouslySelected = field
            field = index
            if(index != null){
                notifyItemChanged(index)
            }
            if(previouslySelected != null){
                notifyItemChanged(previouslySelected)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ImageListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(index: Int){
            val resources = binding.root.resources
            val card = binding.card
            if(index == selectedItemIndex){
                card.cardElevation =
                    resources.getDimension(R.dimen.card_elevation_selected)
                card.setCardBackgroundColor(
                    resources.getColor(R.color.blue_100,null))
            }else{
                card.cardElevation =
                    resources.getDimension(R.dimen.card_elevation_default)
                card.setCardBackgroundColor(
                    resources.getColor(R.color.white,null))
            }
            val fileName = getItem(index)
            binding.fileName = fileName
            binding.card.setOnClickListener {
                selectedItemIndex = index
                onSelect(fileName)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ImageListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getItem(index: Int): String{
        return list!![index]
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


}
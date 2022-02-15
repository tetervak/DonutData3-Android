package ca.tetervak.donutdata3.ui.selectimage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.SelectImageItemBinding

class SelectImageAdapter() : ListAdapter<ImageListItemUiState, SelectImageAdapter.ViewHolder>(
    ImageListItemUiStateDiffCallback()
) {

    class ViewHolder(
        private val binding: SelectImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uiState: ImageListItemUiState) {

            binding.fileName = uiState.fileName

            binding.card.setOnClickListener {
                uiState.onSelect()
            }

            val resources = binding.root.resources
            val card = binding.card
            if (uiState.isSelected) {
                card.cardElevation =
                    resources.getDimension(R.dimen.card_elevation_selected)
                card.setCardBackgroundColor(
                    resources.getColor(R.color.blue_100, null)
                )
            } else {
                card.cardElevation =
                    resources.getDimension(R.dimen.card_elevation_default)
                card.setCardBackgroundColor(
                    resources.getColor(R.color.white, null)
                )
            }

            binding.executePendingBindings()
        }
    }

    class ImageListItemUiStateDiffCallback : DiffUtil.ItemCallback<ImageListItemUiState>() {

        override fun areItemsTheSame(
            oldItem: ImageListItemUiState,
            newItem: ImageListItemUiState
        ): Boolean {
            return oldItem.fileName == newItem.fileName
        }

        override fun areContentsTheSame(
            oldItem: ImageListItemUiState,
            newItem: ImageListItemUiState
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SelectImageItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
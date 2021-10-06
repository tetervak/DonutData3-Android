package ca.tetervak.donutdata3.ui.donutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.tetervak.donutdata3.R
import ca.tetervak.donutdata3.databinding.DonutListItemBinding
import ca.tetervak.donutdata3.domain.Donut

/**
 * The adapter used by the RecyclerView to display the current list of donuts
 */
class DonutListAdapter(
    private var onEdit: (Donut) -> Unit,
    private var onDelete: (Donut) -> Unit) :
    ListAdapter<Donut, DonutListAdapter.ViewHolder>(DonutDiffCallback()) {

    inner class ViewHolder(
        private val binding: DonutListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(donut: Donut) {
            binding.donut = donut
            binding.thumbnail.setImageResource(R.drawable.donut_with_sprinkles)
            binding.deleteButton.setOnClickListener {
                onDelete(donut)
            }
            binding.root.setOnClickListener {
                onEdit(donut)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DonutListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DonutDiffCallback : DiffUtil.ItemCallback<Donut>() {
        override fun areItemsTheSame(oldItem: Donut, newItem: Donut): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donut, newItem: Donut): Boolean {
            return oldItem == newItem
        }
    }
}
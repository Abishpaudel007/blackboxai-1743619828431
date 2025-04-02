package com.example.mealmate.ui.grocery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmate.data.entity.GroceryItemEntity
import com.example.mealmate.databinding.ItemGroceryBinding

class GroceryAdapter(
    private val onItemClick: (GroceryItemEntity) -> Unit,
    private val onItemDelete: (GroceryItemEntity) -> Unit
) : ListAdapter<GroceryItemEntity, GroceryAdapter.GroceryViewHolder>(GroceryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val binding = ItemGroceryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroceryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroceryViewHolder(
        private val binding: ItemGroceryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroceryItemEntity) {
            binding.apply {
                nameTextView.text = item.name
                quantityTextView.text = item.quantity
                categoryTextView.text = item.category
                checkBox.isChecked = item.isPurchased

                checkBox.setOnClickListener {
                    onItemClick(item)
                }

                root.setOnClickListener {
                    checkBox.toggle()
                    onItemClick(item.copy(isPurchased = checkBox.isChecked))
                }
            }
        }
    }

    fun deleteItem(position: Int) {
        val item = getItem(position)
        onItemDelete(item)
    }
}

class GroceryDiffCallback : DiffUtil.ItemCallback<GroceryItemEntity>() {
    override fun areItemsTheSame(oldItem: GroceryItemEntity, newItem: GroceryItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroceryItemEntity, newItem: GroceryItemEntity): Boolean {
        return oldItem == newItem
    }
}
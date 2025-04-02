package com.example.mealmate.ui.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmate.R
import com.example.mealmate.data.entity.RecipeEntity
import com.example.mealmate.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val onItemClick: (RecipeEntity) -> Unit,
    private val onFavoriteClick: (RecipeEntity) -> Unit
) : ListAdapter<RecipeEntity, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecipeViewHolder(
        private val binding: ItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
            binding.apply {
                nameTextView.text = recipe.name
                favoriteButton.isChecked = recipe.isFavorite

                Glide.with(root.context)
                    .load(recipe.imagePath)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(recipeImageView)

                root.setOnClickListener {
                    onItemClick(recipe)
                }

                favoriteButton.setOnClickListener {
                    favoriteButton.isChecked = !favoriteButton.isChecked
                    onFavoriteClick(recipe)
                }
            }
        }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeEntity>() {
    override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
        return oldItem == newItem
    }
}
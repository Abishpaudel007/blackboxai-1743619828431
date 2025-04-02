package com.example.mealmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.mealmate.data.entity.RecipeEntity

@Entity(
    tableName = "grocery_items",
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class GroceryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String,
    val category: String,
    val isPurchased: Boolean = false,
    val recipeId: Long = RecipeEntity.DEFAULT_RECIPE_ID
) {
    companion object {
        const val CATEGORY_VEGETABLES = "Vegetables"
        const val CATEGORY_FRUITS = "Fruits"
        const val CATEGORY_DAIRY = "Dairy"
        const val CATEGORY_MEAT = "Meat"
        const val CATEGORY_GRAINS = "Grains"
        const val CATEGORY_OTHER = "Other"
    }
}
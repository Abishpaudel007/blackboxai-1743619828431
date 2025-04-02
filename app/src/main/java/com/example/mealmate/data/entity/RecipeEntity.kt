package com.example.mealmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val ingredients: String, // JSON string of ingredients list
    val instructions: String,
    val imagePath: String? = null,
    val createdAt: Long = Date().time,
    val isFavorite: Boolean = false
) {
    companion object {
        const val DEFAULT_RECIPE_ID = -1L
    }
}
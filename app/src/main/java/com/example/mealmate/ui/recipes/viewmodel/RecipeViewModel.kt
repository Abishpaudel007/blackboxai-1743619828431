package com.example.mealmate.ui.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.dao.RecipeDao
import com.example.mealmate.data.entity.RecipeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeDao: RecipeDao
) : ViewModel() {

    val allRecipes = recipeDao.getAllRecipes().asLiveData()
    val favoriteRecipes = recipeDao.getFavoriteRecipes().asLiveData()

    fun insertRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.insertRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.updateRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.deleteRecipe(recipe)
        }
    }

    fun toggleFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
        }
    }

    fun searchRecipes(query: String) = recipeDao.searchRecipes("%$query%").asLiveData()

    fun getRecipeById(recipeId: Long) = recipeDao.getRecipeById(recipeId).asLiveData()
}
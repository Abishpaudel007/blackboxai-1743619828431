package com.example.mealmate.ui.recipes

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mealmate.R
import com.example.mealmate.databinding.ActivityAddRecipeBinding
import com.example.mealmate.ui.recipes.viewmodel.RecipeViewModel
import com.example.mealmate.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var imageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.recipeImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_recipe)
    }

    private fun setupListeners() {
        binding.recipeImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.saveButton.setOnClickListener {
            if (validateInputs()) {
                saveRecipe()
            }
        }
    }

    private fun validateInputs(): Boolean {
        with(binding) {
            if (recipeNameEditText.text.isNullOrEmpty()) {
                showToast(getString(R.string.error_recipe_name_empty))
                return false
            }
            if (ingredientsEditText.text.isNullOrEmpty()) {
                showToast(getString(R.string.error_ingredients_empty))
                return false
            }
            if (instructionsEditText.text.isNullOrEmpty()) {
                showToast(getString(R.string.error_instructions_empty))
                return false
            }
            return true
        }
    }

    private fun saveRecipe() {
        with(binding) {
            val recipe = RecipeEntity(
                name = recipeNameEditText.text.toString(),
                ingredients = ingredientsEditText.text.toString(),
                instructions = instructionsEditText.text.toString(),
                imagePath = imageUri?.toString() ?: "",
                isFavorite = favoriteSwitch.isChecked,
                category = categorySpinner.selectedItem.toString()
            )
            viewModel.insertRecipe(recipe)
            showToast(getString(R.string.recipe_saved))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
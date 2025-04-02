package com.example.mealmate.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentRecipeDetailBinding
import com.example.mealmate.ui.recipes.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRecipeById(args.recipeId).observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                binding.apply {
                    recipeName.text = it.name
                    Glide.with(requireContext())
                        .load(it.imagePath)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(recipeImage)
                    
                    ingredientsText.text = it.ingredients
                    instructionsText.text = it.instructions
                    
                    favoriteButton.isChecked = it.isFavorite
                    favoriteButton.setOnClickListener {
                        viewModel.toggleFavorite(recipe)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
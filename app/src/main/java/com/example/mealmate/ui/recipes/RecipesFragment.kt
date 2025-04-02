package com.example.mealmate.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentRecipesBinding
import com.example.mealmate.ui.recipes.adapter.RecipeAdapter
import com.example.mealmate.ui.recipes.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(
            onItemClick = { recipe -> 
                val action = RecipesFragmentDirections.actionNavigationRecipesToRecipeDetailFragment(recipe.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { recipe -> viewModel.toggleFavorite(recipe) }
        )

        binding.recyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = this@RecipesFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
            binding.emptyState.visibility = if (recipes.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_recipes_to_addRecipeActivity)
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchRecipes(it) }
                return true
            }
        })
    }

    private fun navigateToRecipeDetail(recipe: RecipeEntity) {
        // Will implement recipe detail navigation in next step
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
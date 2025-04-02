package com.example.mealmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addRecipeActivity)
        }

        binding.viewGroceryButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_grocery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
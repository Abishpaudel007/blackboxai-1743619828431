package com.example.mealmate.ui.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentGroceryBinding
import com.example.mealmate.ui.grocery.adapter.GroceryAdapter
import com.example.mealmate.ui.grocery.callback.GroceryItemTouchHelperCallback
import com.example.mealmate.ui.grocery.viewmodel.GroceryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroceryFragment : Fragment() {
    private var _binding: FragmentGroceryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroceryViewModel by viewModels()
    private lateinit var adapter: GroceryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroceryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = GroceryAdapter(
            onItemClick = { item -> toggleItemStatus(item) },
            onDeleteClick = { item -> deleteItem(item) }
        )

        binding.recyclerView.apply {
            adapter = this@GroceryFragment.adapter
            setHasFixedSize(true)
        }

        val itemTouchHelper = ItemTouchHelper(GroceryItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupObservers() {
        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            binding.emptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.addButton.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun toggleItemStatus(item: GroceryItemEntity) {
        viewModel.updateItem(item.copy(isChecked = !item.isChecked))
    }

    private fun deleteItem(item: GroceryItemEntity) {
        viewModel.deleteItem(item)
        showUndoSnackbar(item)
    }

    private fun showUndoSnackbar(item: GroceryItemEntity) {
        Snackbar.make(binding.root, R.string.item_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                viewModel.insertItem(item)
            }
            .show()
    }

    private fun showAddItemDialog() {
        val dialog = AddGroceryItemDialogFragment { name, category ->
            viewModel.insertItem(
                GroceryItemEntity(
                    name = name,
                    category = category,
                    quantity = "1",
                    isChecked = false
                )
            )
        }
        dialog.show(parentFragmentManager, "AddGroceryItemDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
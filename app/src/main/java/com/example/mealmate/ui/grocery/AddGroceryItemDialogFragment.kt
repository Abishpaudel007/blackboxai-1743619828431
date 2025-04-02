package com.example.mealmate.ui.grocery

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.mealmate.R
import com.example.mealmate.databinding.DialogAddGroceryItemBinding

class AddGroceryItemDialogFragment(
    private val onAddItem: (String, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogAddGroceryItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddGroceryItemBinding.inflate(LayoutInflater.from(context))

        setupCategorySpinner()
        setupButtons()

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_new_item)
            .setView(binding.root)
            .create()
    }

    private fun setupCategorySpinner() {
        val categories = resources.getStringArray(R.array.grocery_categories)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.categorySpinner.adapter = adapter
    }

    private fun setupButtons() {
        binding.addButton.setOnClickListener {
            val name = binding.itemNameEditText.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            
            if (name.isNotBlank()) {
                onAddItem(name, category)
                dismiss()
            } else {
                binding.itemNameEditText.error = getString(R.string.error_item_name_empty)
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}
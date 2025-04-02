package com.example.mealmate.util

import com.example.mealmate.data.entity.GroceryItemEntity

fun formatGroceryList(items: List<GroceryItemEntity>): String {
    return buildString {
        append("📋 Grocery List\n\n")
        items.groupBy { it.category }.forEach { (category, items) ->
            append("$category:\n")
            items.forEach { item ->
                append("- ${item.name} (${item.quantity})\n")
            }
            append("\n")
        }
        append("Happy shopping! 🛒")
    }
}
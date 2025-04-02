package com.example.mealmate.ui.grocery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.dao.GroceryDao
import com.example.mealmate.data.entity.GroceryItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryDao: GroceryDao
) : ViewModel() {

    val activeItems = groceryDao.getActiveItems().asLiveData()
    val purchasedItems = groceryDao.getPurchasedItems().asLiveData()
    val allItems = groceryDao.getAllItems().asLiveData()

    fun insertItem(item: GroceryItemEntity) {
        viewModelScope.launch {
            groceryDao.insertItem(item)
        }
    }

    fun updatePurchaseStatus(itemId: Long, isPurchased: Boolean) {
        viewModelScope.launch {
            groceryDao.updatePurchaseStatus(itemId, isPurchased)
        }
    }

    fun deleteItem(item: GroceryItemEntity) {
        viewModelScope.launch {
            groceryDao.deleteItem(item)
        }
    }

    fun clearPurchasedItems() {
        viewModelScope.launch {
            groceryDao.clearPurchasedItems()
        }
    }
}
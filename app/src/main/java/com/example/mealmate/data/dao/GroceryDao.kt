package com.example.mealmate.data.dao

import androidx.room.*
import com.example.mealmate.data.entity.GroceryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: GroceryItemEntity): Long

    @Update
    suspend fun updateItem(item: GroceryItemEntity)

    @Delete
    suspend fun deleteItem(item: GroceryItemEntity)

    @Query("DELETE FROM grocery_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Long)

    @Query("SELECT * FROM grocery_items WHERE isPurchased = 0 ORDER BY category ASC, name ASC")
    fun getActiveItems(): Flow<List<GroceryItemEntity>>

    @Query("SELECT * FROM grocery_items WHERE isPurchased = 1 ORDER BY category ASC, name ASC")
    fun getPurchasedItems(): Flow<List<GroceryItemEntity>>

    @Query("SELECT * FROM grocery_items ORDER BY category ASC, name ASC")
    fun getAllItems(): Flow<List<GroceryItemEntity>>

    @Query("UPDATE grocery_items SET isPurchased = :isPurchased WHERE id = :itemId")
    suspend fun updatePurchaseStatus(itemId: Long, isPurchased: Boolean)

    @Query("DELETE FROM grocery_items WHERE isPurchased = 1")
    suspend fun clearPurchasedItems()

    @Query("SELECT DISTINCT category FROM grocery_items ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>
}
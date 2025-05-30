package com.app.shopping.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Insert
    suspend fun insert(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<ShoppingItem>>

    @Query("SELECT name FROM categories")
    fun getAllCategories(): Flow<List<String>>

    @Insert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT COUNT(*) FROM shopping_items WHERE category = :categoryName")
    suspend fun getItemCountForCategory(categoryName: String): Int
}

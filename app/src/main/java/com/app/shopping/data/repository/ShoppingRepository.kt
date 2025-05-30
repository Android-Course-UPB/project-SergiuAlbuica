package com.app.shopping.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.app.shopping.data.Category
import com.app.shopping.data.ShoppingItem
import com.app.shopping.data.ShoppingItemDao
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val shoppingItemDao: ShoppingItemDao) {
    val allItems: Flow<List<ShoppingItem>> = shoppingItemDao.getAllItems()
    val allCategories: Flow<List<String>> = shoppingItemDao.getAllCategories()

    suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insert(item)
    }

    suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item)
    }

    suspend fun update(item: ShoppingItem) {
        shoppingItemDao.update(item)
    }

    suspend fun insertCategory(category: Category) {
        shoppingItemDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        shoppingItemDao.deleteCategory(category)
    }

    suspend fun getItemCountForCategory(categoryName: String): Int {
        return shoppingItemDao.getItemCountForCategory(categoryName)
    }

    fun isCategoryInUse(category: String): LiveData<Boolean> {
        return liveData {
            val count = getItemCountForCategory(category)
            emit(count > 0)
        }
    }
}

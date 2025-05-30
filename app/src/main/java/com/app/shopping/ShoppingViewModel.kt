package com.app.shopping

import android.app.Application
import androidx.lifecycle.*
import com.app.shopping.api.Product
import com.app.shopping.api.ProductRepository
import com.app.shopping.data.AppDatabase
import com.app.shopping.data.Category
import com.app.shopping.data.ShoppingItem
import com.app.shopping.data.repository.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShoppingRepository
    val allItems: LiveData<List<ShoppingItem>>
    var allCategories = MutableLiveData<List<String>>()

    init {
        val dao = AppDatabase.getDatabase(application).shoppingItemDao()
        repository = ShoppingRepository(dao)
        allItems = repository.allItems.asLiveData()
        allCategories = MutableLiveData()

        viewModelScope.launch {
            repository.allCategories.collect { categories ->
                allCategories.postValue(categories)
            }
        }
    }

    fun insert(item: ShoppingItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun delete(item: ShoppingItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun update(item: ShoppingItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }

    fun getItemCountForCategory(categoryName: String): LiveData<Int> {
        return liveData {
            emit(repository.getItemCountForCategory(categoryName))
        }
    }

    fun isCategoryInUse(category: String): LiveData<Boolean> {
        return repository.isCategoryInUse(category)
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            repository.deleteCategory(Category(category))
        }
    }

    private val productRepository = ProductRepository()
    private val _searchResults = MutableLiveData<List<Product>>(emptyList())
    val searchResults: LiveData<List<Product>> = _searchResults

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = productRepository.searchProducts(query)
        }
    }
}
package com.app.shopping.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalProductDataSource {
    fun getLocalSuggestions(query: String): List<Product> {
        val lowerQuery = query.lowercase()
        return COMMON_PRODUCTS.filter {
            it.productName.lowercase().contains(lowerQuery)
        }.take(5)
    }

    companion object {
        private val COMMON_PRODUCTS = listOf(
            Product("Pizza Margherita", "Pizza Brand", "400g", "Frozen Foods,Pizza"),
            Product("Pizza Pepperoni", "Pizza Brand", "450g", "Frozen Foods,Pizza"),
            Product("Milk", "Dairy Farm", "1L", "Dairy,Beverages"),
            Product("Eggs", "Farm Fresh", "12 units", "Dairy,Breakfast"),
            Product("Bread", "Bakery", "500g", "Bakery,Bread"),
            Product("Chicken Breast", "Meat Co", "500g", "Meat,Poultry"),
            Product("Apple", "Fresh Farms", "1kg", "Fruits,Fresh Produce"),
            Product("Bananas", "Tropical Farms", "1kg", "Fruits,Fresh Produce"),
            Product("Tomatoes", "Garden Fresh", "500g", "Vegetables,Fresh Produce"),
            Product("Pasta", "Italian Foods", "500g", "Pasta,Dry Goods"),
            Product("Rice", "Grain Co", "1kg", "Grains,Dry Goods"),
            Product("Cereal", "Breakfast Co", "500g", "Breakfast,Dry Goods"),
            Product("Coffee", "Coffee Co", "250g", "Beverages,Coffee"),
            Product("Tea", "Tea Co", "50 bags", "Beverages,Tea")
        )
    }
}

class ProductRepository {
    private val apiService = ProductApiService.create()
    private val localDataSource = LocalProductDataSource()
    private val TAG = "ProductRepository"

    suspend fun searchProducts(query: String): List<Product> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching for products with query: $query")
                val response = apiService.searchProducts(query)
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    Log.d(TAG, "Found ${products.size} products")
                    if (products.isNotEmpty()) {
                        return@withContext products
                    }
                }
                Log.d(TAG, "Using local fallback data")
                return@withContext localDataSource.getLocalSuggestions(query)
            } catch (e: Exception) {
                Log.e(TAG, "Exception during API call", e)
                Log.d(TAG, "Using local fallback data after exception")
                return@withContext localDataSource.getLocalSuggestions(query)
            }
        }
    }
}
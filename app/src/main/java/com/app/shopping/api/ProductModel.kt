package com.app.shopping.api

import com.google.gson.annotations.SerializedName

data class ProductSearchResponse(
    val products: List<Product> = emptyList(),
    val count: Int = 0
)

data class Product(
    @SerializedName("product_name") val productName: String = "",
    @SerializedName("brands") val brands: String = "",
    @SerializedName("quantity") val quantity: String = "",
    @SerializedName("categories") val categories: String = ""
)

package com.app.shopping

import com.app.shopping.api.Product
import com.app.shopping.api.ProductSearchResponse
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductModelTest {

    @Test
    fun testProductDeserialization() {
        val json = """
            {
                "product_name": "Pizza Margherita",
                "brands": "Pizza Brand",
                "quantity": "400g",
                "categories": "Frozen Foods,Pizza"
            }
        """.trimIndent()

        val product = Gson().fromJson(json, Product::class.java)

        assertEquals("Pizza Margherita", product.productName)
        assertEquals("Pizza Brand", product.brands)
        assertEquals("400g", product.quantity)
        assertEquals("Frozen Foods,Pizza", product.categories)
    }

    @Test
    fun testProductSearchResponseDeserialization() {
        val json = """
            {
                "count": 2,
                "products": [
                    {
                        "product_name": "Pizza Margherita",
                        "brands": "Pizza Brand",
                        "quantity": "400g",
                        "categories": "Frozen Foods,Pizza"
                    },
                    {
                        "product_name": "Pizza Pepperoni",
                        "brands": "Pizza Brand",
                        "quantity": "450g",
                        "categories": "Frozen Foods,Pizza"
                    }
                ]
            }
        """.trimIndent()

        val response = Gson().fromJson(json, ProductSearchResponse::class.java)

        assertEquals(2, response.count)
        assertEquals(2, response.products.size)
        assertEquals("Pizza Margherita", response.products[0].productName)
        assertEquals("Pizza Pepperoni", response.products[1].productName)
    }
}
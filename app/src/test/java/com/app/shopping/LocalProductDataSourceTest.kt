package com.app.shopping

import com.app.shopping.api.LocalProductDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalProductDataSourceTest {

    private val dataSource = LocalProductDataSource()

    @Test
    fun testGetLocalSuggestions_exactMatch() {
        val results = dataSource.getLocalSuggestions("Pizza")

        assertEquals(2, results.size)
        assertTrue(results.all { it.productName.contains("Pizza", ignoreCase = true) })
    }

    @Test
    fun testGetLocalSuggestions_partialMatch() {
        val results = dataSource.getLocalSuggestions("Br")

        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.productName.contains("Bread", ignoreCase = true) })
    }

    @Test
    fun testGetLocalSuggestions_noMatch() {
        val results = dataSource.getLocalSuggestions("xyz123")

        assertTrue(results.isEmpty())
    }

    @Test
    fun testGetLocalSuggestions_limitTo5() {
        val results = dataSource.getLocalSuggestions("a")

        assertTrue(results.size <= 5)
    }
}
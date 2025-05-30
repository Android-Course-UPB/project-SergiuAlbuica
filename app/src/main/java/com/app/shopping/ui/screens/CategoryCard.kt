package com.app.shopping.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.shopping.data.Category

@Composable
fun CategoryCard(
    category: String,
    onDeleteClick: (String) -> Unit
) {
    val backgroundColor = remember(category) {
        val categoryColors = mapOf(
            "Fruits" to Color(0xFFE57373),        // Light Red
            "Vegetables" to Color(0xFF81C784),    // Light Green
            "Dairy" to Color(0xFF64B5F6),         // Light Blue
            "Meat" to Color(0xFFFFB74D),          // Light Orange
            "Bakery" to Color(0xFFBA68C8),        // Light Purple
            "Drinks" to Color(0xFF4FC3F7),        // Lighter Blue
            "Snacks" to Color(0xFFFFD54F),        // Light Yellow
            "Cleaning" to Color(0xFF4DD0E1),      // Light Teal
            "Personal Care" to Color(0xFFAED581), // Light Lime
            "Other" to Color(0xFFB0BEC5)          // Light Blue Grey
        )

        categoryColors[category] ?: run {
            val hue = (category.hashCode() and 0xFFFFFF) % 360f
            Color.hsl(hue, 0.7f, 0.8f)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 4.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.h6
                )
            }

            IconButton(onClick = { onDeleteClick(category) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Category")
            }
        }
    }
}
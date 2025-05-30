package com.app.shopping.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.shopping.ShoppingViewModel
import com.app.shopping.data.ShoppingItem
import com.app.shopping.ui.dialogs.AddEditItemDialog

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel,
    onNavigateToCategories: () -> Unit
) {
    val items by viewModel.allItems.observeAsState(emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ShoppingItem?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(
                        onClick = onNavigateToCategories,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Manage Categories",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items) { item ->
                ShoppingItemCard(
                    item = item,
                    onItemClick = { itemToEdit = item },
                    onDeleteClick = { viewModel.delete(it) }
                )
            }
        }

        if (showAddDialog) {
            AddEditItemDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, quantity, store, category ->
                    viewModel.insert(
                        ShoppingItem(
                            name = name,
                            quantity = quantity,
                            store = store,
                            category = category
                        )
                    )
                },
                viewModel = viewModel
            )
        }

        itemToEdit?.let { item ->
            AddEditItemDialog(
                onDismiss = { itemToEdit = null },
                onSave = { name, quantity, store, category ->
                    viewModel.update(
                        item.copy(
                            name = name,
                            quantity = quantity,
                            store = store,
                            category = category
                        )
                    )
                },
                viewModel = viewModel,
                item = item
            )
        }
    }
}

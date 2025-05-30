package com.app.shopping.ui.dialogs

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.shopping.ShoppingViewModel
import com.app.shopping.data.Category
import com.app.shopping.data.ShoppingItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.zIndex

@Composable
fun AddEditItemDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, quantity: Int, store: String, category: String) -> Unit,
    viewModel: ShoppingViewModel,
    item: ShoppingItem? = null
) {
    val categories by viewModel.allCategories.observeAsState(emptyList())
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var quantityErrorMessage by remember { mutableStateOf("") }

    var name by remember { mutableStateOf(item?.name ?: "") }
    var quantity by remember { mutableStateOf(item?.quantity?.toString() ?: "1") }
    var store by remember { mutableStateOf(item?.store ?: "") }
    var selectedCategory by remember { mutableStateOf(item?.category ?: "") }
    var expandedDropdown by remember { mutableStateOf(false) }
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    var showSuggestions by remember { mutableStateOf(false) }

    LaunchedEffect(searchResults) {
        Log.d("AddEditItemDialog", "Search results updated: ${searchResults.size} items")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (item == null) "Add Item" else "Edit Item",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(16.dp))

                var isLoading by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.length > 2) {
                            Log.d("AddEditItemDialog", "Searching for: $it")
                            viewModel.searchProducts(it)
                            showSuggestions = true
                        } else {
                            showSuggestions = false
                        }
                    },
                    label = { Text("Item Name") },
                    trailingIcon = {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                LaunchedEffect(searchResults) {
                    isLoading = false
                }

                if (showSuggestions && searchResults.isNotEmpty()) {
                    Log.d("AddEditItemDialog", "Showing ${searchResults.size} suggestions")

                    searchResults.forEach { product ->
                        Log.d("AddEditItemDialog", "Product: ${product.productName}, Category: ${product.categories}")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 150.dp)
                            ) {
                                items(searchResults) { product ->
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                name = product.productName

                                                if (product.categories.isNotEmpty()) {
                                                    val categoryList = product.categories.split(",")
                                                    if (categoryList.isNotEmpty()) {
                                                        selectedCategory = categoryList.first().trim()
                                                    }
                                                }

                                                showSuggestions = false
                                            }
                                    ) {
                                        Text(
                                            text = product.productName.ifEmpty { "Unknown product" },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        )
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            quantity = input

                            val intValue = input.toIntOrNull()
                            if (input.isEmpty()) {
                                quantityError = true
                                quantityErrorMessage = "Quantity is required"
                            } else if (intValue == null || intValue <= 0) {
                                quantityError = true
                                quantityErrorMessage = "Please enter a positive number"
                            } else {
                                quantityError = false
                                quantityErrorMessage = ""
                            }
                        }
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = quantityError
                )

                if (quantityError) {
                    Text(
                        text = quantityErrorMessage,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = store,
                    onValueChange = { store = it },
                    label = { Text("Store") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (categories.isNotEmpty()) {
                    Text("Category", style = MaterialTheme.typography.caption)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDropdown = true }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = { },
                            label = { Text("Select a category") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false
                        )

                        DropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedCategory = category
                                        expandedDropdown = false
                                    }
                                ) {
                                    Text(category)
                                }
                            }
                        }
                    }

                    if (item == null) {
                        TextButton(onClick = { showAddCategoryDialog = true }) {
                            Text("Add New Category")
                        }
                    }
                } else {
                    Text("No categories available. Add one first.")
                    TextButton(onClick = { showAddCategoryDialog = true }) {
                        Text("Add New Category")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (name.isNotEmpty() && selectedCategory.isNotEmpty() && !quantityError) {
                                onSave(
                                    name,
                                    quantity.toIntOrNull() ?: 1,
                                    store,
                                    selectedCategory
                                )
                                onDismiss()
                            }
                        },
                        enabled = name.isNotEmpty() && selectedCategory.isNotEmpty() && !quantityError && quantity.isNotEmpty()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onSave = { newCategory ->
                viewModel.insertCategory(Category(newCategory))
                selectedCategory = newCategory
                showAddCategoryDialog = false
            },
            viewModel = viewModel
        )
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    viewModel: ShoppingViewModel
) {
    var categoryName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val categories by viewModel.allCategories.observeAsState(emptyList())

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add New Category",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        errorMessage = null
                    },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (categoryName.isNotEmpty()) {
                                if (categories.contains(categoryName)) {
                                    errorMessage = "Category already exists"
                                } else {
                                    onSave(categoryName)
                                }
                            }
                        },
                        enabled = categoryName.isNotEmpty()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
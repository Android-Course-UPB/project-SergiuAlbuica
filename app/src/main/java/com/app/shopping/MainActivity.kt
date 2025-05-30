package com.app.shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.shopping.ui.screens.CategoryManagementScreen
import com.app.shopping.ui.screens.ShoppingListScreen
import com.app.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ShoppingApp()
                }
            }
        }
    }
}

@Composable
fun ShoppingApp() {
    val viewModel: ShoppingViewModel = viewModel()
    AppNavigation(viewModel = viewModel)
}

@Composable
fun AppNavigation(viewModel: ShoppingViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "shopping_list") {
        composable("shopping_list") {
            ShoppingListScreen(
                viewModel = viewModel,
                onNavigateToCategories = {
                    navController.navigate("categories")
                }
            )
        }

        composable("categories") {
            CategoryManagementScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
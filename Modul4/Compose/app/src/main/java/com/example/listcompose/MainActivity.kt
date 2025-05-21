package com.example.listcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.listcompose.ui.theme.ListComposeTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContent {
            ListComposeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        ItemListScreen(navController)
                    }
                    composable(
                        "detail/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                        val context = this@MainActivity
                        val items = getItems(context)
                        DetailScreen(item = items[itemId])
                    }
                }
            }
        }
    }
}
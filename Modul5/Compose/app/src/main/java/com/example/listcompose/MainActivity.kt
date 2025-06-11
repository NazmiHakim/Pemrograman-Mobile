package com.example.listcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.listcompose.data.local.AppDatabase
import com.example.listcompose.data.preferences.PreferencesManager
import com.example.listcompose.data.remote.MovieRepository
import com.example.listcompose.data.remote.RetrofitInstance
import com.example.listcompose.ui.screens.*
import com.example.listcompose.ui.theme.ListComposeTheme
import com.example.listcompose.viewmodel.MovieViewModel
import com.example.listcompose.viewmodel.MovieViewModelFactory
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        val context = this
        val database = AppDatabase.getInstance(context)
        val repository = MovieRepository(
            apiService = RetrofitInstance.api,
            movieDao = database.movieDao()
        )
        val preferencesManager = PreferencesManager(context)
        val viewModelFactory = MovieViewModelFactory(application, repository, preferencesManager)

        setContent {
            ListComposeTheme {
                val navController = rememberNavController()
                val movieViewModel: MovieViewModel = viewModel(factory = viewModelFactory)
                val username = movieViewModel.getUsername()

                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            username = username,
                            onNavigateToMovies = {
                                movieViewModel.loadMovies()
                                navController.navigate("movies")
                            },
                            onNavigateToLocalMovies = {
                                navController.navigate("local_movies")
                            },
                            onNavigateToSettings = {
                                navController.navigate("settings")
                            },
                            navController = navController,
                            viewModel = movieViewModel
                        )
                    }

                    composable("movies") {
                        MovieListScreen(
                            navController = navController,
                            viewModel = movieViewModel,
                            isLocal = false
                        )
                    }

                    composable("local_movies") {
                        MovieListScreen(
                            navController = navController,
                            viewModel = movieViewModel,
                            isLocal = true
                        )
                    }

                    composable(
                        "movie_detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        MovieDetailScreen(
                            movieId = id,
                            viewModel = movieViewModel
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            viewModel = movieViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
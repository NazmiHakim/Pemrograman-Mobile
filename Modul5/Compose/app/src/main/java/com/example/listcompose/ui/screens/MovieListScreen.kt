package com.example.listcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.listcompose.R
import com.example.listcompose.data.model.Movie
import com.example.listcompose.ui.components.MovieCard
import com.example.listcompose.viewmodel.MovieViewModel

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    isLocal: Boolean
) {
    val movieState by viewModel.movieState.collectAsState()
    val movies by if (isLocal) {
        viewModel.localMovies.collectAsState(initial = emptyList())
    } else {
        viewModel.movieList.collectAsState()
    }
    val scrollState = rememberLazyListState()
    val username by remember { derivedStateOf { viewModel.getUsername() } }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() &&
                    visibleItems.last().index >= movies.size - 5 &&
                    !isLocal &&
                    movieState !is MovieViewModel.MovieState.Loading
                ) {
                    viewModel.loadMovies()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (!isLocal) {
            Button(
                onClick = { viewModel.loadMovies(forceRefresh = true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = movieState !is MovieViewModel.MovieState.Loading
            ) {
                Text(stringResource(R.string.refresh_movies))
            }
        }

        if (username.isNotEmpty()) {
            Text(
                text = stringResource(R.string.greeting_user, username),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        when (val state = movieState) {
            is MovieViewModel.MovieState.Loading -> {
                if (movies.isEmpty()) {
                    LoadingView()
                } else {
                    MovieListView(movies, navController, isLocal, scrollState, viewModel)
                    LoadingView(modifier = Modifier.fillMaxWidth())
                }
            }

            is MovieViewModel.MovieState.Error -> {
                ErrorView(state.message)
                if (state.hasData) {
                    MovieListView(movies, navController, isLocal, scrollState, viewModel)
                } else {
                    EmptyView(isLocal)
                }
            }

            is MovieViewModel.MovieState.Success -> {
                if (state.isRefreshed) {
                    LaunchedEffect(Unit) {
                        scrollState.scrollToItem(0)
                    }
                }

                if (movies.isEmpty()) {
                    EmptyView(isLocal)
                } else {
                    MovieListView(movies, navController, isLocal, scrollState, viewModel)
                }
            }
        }
    }
}

@Composable
private fun MovieListView(
    movies: List<Movie>,
    navController: NavController,
    isLocal: Boolean,
    scrollState: LazyListState,
    viewModel: MovieViewModel
) {
    LazyColumn(state = scrollState) {
        items(movies) { movie ->
            MovieCard(
                movie = movie,
                onDetailClick = {
                    if (isLocal) {
                        viewModel.saveLastViewedMovieId(movie.id)
                    }
                    navController.navigate("movie_detail/${movie.id}")
                },
                onSaveClick = if (!isLocal) {
                    { viewModel.saveMovieToLocal(movie) }
                } else null
            )
        }
    }
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.loading_movies))
        }
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error_prefix, message),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun EmptyView(isLocal: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(
                if (isLocal) R.string.no_local_movies else R.string.no_remote_movies
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
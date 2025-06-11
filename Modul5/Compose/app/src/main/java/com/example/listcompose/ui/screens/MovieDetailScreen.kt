package com.example.listcompose.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.listcompose.R
import com.example.listcompose.data.model.Movie
import com.example.listcompose.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieViewModel
) {
    val remoteMovies by viewModel.movieList.collectAsState()
    val localMovies by viewModel.localMovies.collectAsState()
    val context = LocalContext.current

    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(movieId, remoteMovies, localMovies) {
        isLoading = true
        error = null

        val localMovie = localMovies.find { it.id == movieId }
        if (localMovie != null) {
            movie = localMovie
            viewModel.saveLastViewedMovieId(movieId)
            isLoading = false
            return@LaunchedEffect
        }

        val remoteMovie = remoteMovies.find { it.id == movieId }
        if (remoteMovie != null) {
            movie = remoteMovie
            isLoading = false
            return@LaunchedEffect
        }

        var errorResId: Int? = null

        try {
            val dbMovie = viewModel.getMovieById(movieId)
            if (dbMovie != null) {
                movie = dbMovie
                if (dbMovie.isLocal) {
                    viewModel.saveLastViewedMovieId(movieId)
                }
            } else {
                errorResId = R.string.movie_not_found
            }
        } catch (e: Exception) {
            errorResId = R.string.error_loading_movie
        }

        if (errorResId != null) {
            error = context.getString(errorResId)
        }

        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null || movie == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(error ?: stringResource(R.string.movie_data_unavailable))
        }
        return
    }

    val scrollState = rememberScrollState()
    val movieToShow = movie!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://image.tmdb.org/t/p/w500${movieToShow.posterPath}"
            ),
            contentDescription = movieToShow.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movieToShow.title,
            style = MaterialTheme.typography.headlineLarge
        )

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(R.string.rating),
                tint = Color(0xFFFFD700)
            )
            Text(
                text = " ${movieToShow.voteAverage}/10",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = movieToShow.releaseDate,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = movieToShow.overview,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val tmdbUrl = "https://www.themoviedb.org/movie/$movieId"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, tmdbUrl.toUri())
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.unable_to_open_browser),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.view_on_tmdb))
        }
    }
}
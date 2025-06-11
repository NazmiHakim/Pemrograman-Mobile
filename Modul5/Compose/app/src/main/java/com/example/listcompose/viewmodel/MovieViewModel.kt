package com.example.listcompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listcompose.data.model.Movie
import com.example.listcompose.data.preferences.PreferencesManager
import com.example.listcompose.data.remote.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieViewModel(
    application: Application,
    private val repository: MovieRepository,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {

    sealed class MovieState {
        data object Loading : MovieState()
        data class Success(val movies: List<Movie>, val isRefreshed: Boolean) : MovieState()
        data class Error(val message: String, val hasData: Boolean) : MovieState()
    }

    private val _movieState = MutableStateFlow<MovieState>(MovieState.Loading)
    val movieState: StateFlow<MovieState> = _movieState

    val movieList: StateFlow<List<Movie>> = repository.getMovies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val localMovies: StateFlow<List<Movie>> = repository.getLocalMovies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadMovies(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _movieState.value = MovieState.Loading
            try {
                val isRefreshed = repository.refreshMovies(forceRefresh)
                val currentMovies = repository.getMovies().first()
                _movieState.value = MovieState.Success(currentMovies, isRefreshed)
            } catch (e: Exception) {
                val hasData = repository.getMovies().first().isNotEmpty()
                _movieState.value = MovieState.Error(
                    "Failed to load movies: ${e.message ?: "Unknown error"}",
                    hasData
                )
                Timber.e(e, "Error loading movies")
            }
        }
    }

    fun saveLastViewedMovieId(id: Int) {
        viewModelScope.launch {
            preferencesManager.saveLastViewedMovieId(id)
        }
    }

    private fun getLastViewedMovieId(): Int {
        return preferencesManager.getLastViewedMovieId()
    }

    fun getLastViewedMovie(): Movie? {
        val lastId = getLastViewedMovieId()
        if (lastId == -1) return null

        return findMovieById(lastId)?.takeIf {
            movieList.value.any { it.id == lastId } || localMovies.value.any { it.id == lastId }
        }
    }

    fun getUsername(): String {
        return preferencesManager.getUsername()
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            preferencesManager.saveUsername(newUsername)
        }
    }

    fun saveMovieToLocal(movie: Movie) {
        viewModelScope.launch {
            repository.saveMovieToLocal(movie)
        }
    }

    suspend fun getMovieById(id: Int): Movie? {
        return repository.getMovieById(id) ?: findMovieById(id)
    }

    private fun findMovieById(id: Int): Movie? {
        return localMovies.value.find { it.id == id }
            ?: movieList.value.find { it.id == id }
    }
}
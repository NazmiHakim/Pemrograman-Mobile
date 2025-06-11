package com.example.listcompose.data.remote

import com.example.listcompose.BuildConfig
import com.example.listcompose.data.local.MovieDao
import com.example.listcompose.data.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber

class MovieRepository(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao
) {
    companion object {
        private const val CACHE_EXPIRATION_TIME_MS = 30 * 60 * 1000L
    }

    private var currentPage = 1
    private var totalPages = 1

    suspend fun refreshMovies(forceRefresh: Boolean = false): Boolean {
        return try {
            val cachedMovies = movieDao.getAllMovies().first()
            val shouldRefresh = forceRefresh || isCacheExpired() || cachedMovies.isEmpty()

            if (!shouldRefresh && currentPage >= totalPages) {
                return false
            }

            if (shouldRefresh) {
                currentPage = 1
                if (forceRefresh) {
                    movieDao.clearAll()
                }
            }

            val response = apiService.getPopularMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = "en-US",
                page = currentPage
            )

            totalPages = response.totalPages

            response.results.takeIf { it.isNotEmpty() }?.let { movies ->
                val entities = movies.map { movie ->
                    Movie(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.posterPath ?: "",
                        voteAverage = movie.voteAverage,
                        releaseDate = movie.releaseDate,
                        popularity = movie.popularity,
                        lastUpdated = System.currentTimeMillis(),
                        tmdbUrl = "https://www.themoviedb.org/movie/${movie.id}"
                    )
                }
                movieDao.insertMovies(entities)

                if (!forceRefresh && currentPage < totalPages) {
                    currentPage++
                }
            }
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to refresh movies")
            throw e
        }
    }

    private suspend fun isCacheExpired(): Boolean {
        return movieDao.getLastUpdateTime()?.let { lastUpdated ->
            System.currentTimeMillis() - lastUpdated > CACHE_EXPIRATION_TIME_MS
        } ?: true
    }

    fun getMovies(): Flow<List<Movie>> = movieDao.getAllMovies()
        .catch { e ->
            Timber.e(e, "Error loading movies from DB")
            emit(emptyList())
        }

    fun getLocalMovies(): Flow<List<Movie>> = movieDao.getLocalMovies()

    suspend fun saveMovieToLocal(movie: Movie) {
        movieDao.insertMovies(listOf(movie.copy(
            lastUpdated = System.currentTimeMillis(),
            isLocal = true
        )))
}
    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)
    }
}
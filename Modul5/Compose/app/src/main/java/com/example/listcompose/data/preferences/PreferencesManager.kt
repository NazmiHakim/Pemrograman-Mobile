package com.example.listcompose.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_LAST_VIEWED_MOVIE_ID = "last_viewed_movie_id"
    }

    fun saveUsername(username: String) {
        prefs.edit {
            putString(KEY_USERNAME, username)
        }
    }

    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }

    fun saveLastViewedMovieId(movieId: Int) {
        prefs.edit { putInt(KEY_LAST_VIEWED_MOVIE_ID, movieId) }
    }

    fun getLastViewedMovieId(): Int {
        return prefs.getInt(KEY_LAST_VIEWED_MOVIE_ID, -1)
    }
}
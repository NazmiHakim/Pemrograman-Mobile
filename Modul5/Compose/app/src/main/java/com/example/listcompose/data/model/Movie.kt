package com.example.listcompose.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,

    val title: String,
    val overview: String,

    @SerialName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @SerialName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @SerialName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    val popularity: Double,

    // Additional fields for local storage
    @ColumnInfo(name = "is_local")
    val isLocal: Boolean = false,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "tmdb_url")
    val tmdbUrl: String = "",

    // Optional fields from API that we might not always use
    @SerialName("adult")
    val adult: Boolean? = null,

    @SerialName("backdrop_path")
    val backdropPath: String? = null
)

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)
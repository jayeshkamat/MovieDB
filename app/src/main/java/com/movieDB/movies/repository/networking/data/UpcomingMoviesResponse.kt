package com.movieDB.movies.repository.networking.data

data class UpcomingMoviesResponse(
    val dates: Dates?,
    val page: Int,
    val results: List<Results>?,
    val total_pages:Int,
    val total_results:Int
)

data class Dates(
    val maximum: String?,
    val minimum: String?
)

data class Results(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids:List<Int>,
    val id: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean,
    val vote_average: String?,
    val vote_count: String?,
)
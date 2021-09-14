package com.movieDB.movies.repository.networking.data

data class MovieImagesResponse(
    val backdrops: List<ImageData>?,
    val id: String?,
    val logos: List<ImageData>?,
    val posters:List<ImageData>?
)

data class ImageData(
    val aspect_ratio: String?,
    val height: String?,
    val iso_639_1: String?,
    val file_path: String?,
    val vote_average: String?,
    val vote_count: String?,
    val width: String?
)
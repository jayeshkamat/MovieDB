package com.movieDB.movies.repository.networking

import com.movieDB.movies.repository.networking.data.MovieDetailResponse
import com.movieDB.movies.repository.networking.data.MovieImagesResponse
import com.movieDB.movies.repository.networking.data.UpcomingMoviesResponse
import com.movieDB.movies.utilities.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDBService {

    @GET(Constants.BASE_URL + "{movie}"+Constants.UPCOMING)
    @Headers("accept: application/json", "content-type: application/json; charset=utf-8")
    fun getUpcomingMovies(
        @Path("movie", encoded = true) movie: String,
        @Query("api_key") api_key: String,
        @Query("page") page: String
    ): Call<UpcomingMoviesResponse>

    @GET(Constants.BASE_URL + "{movie}"+"/{movieId}/"+Constants.IMAGES)
    @Headers("accept: application/json", "content-type: application/json; charset=utf-8")
    fun getMovieImages(
        @Path("movie", encoded = true) movie: String,
        @Path("movieId", encoded = true) movieId: String,
        @Query("api_key") api_key: String
    ): Call<MovieImagesResponse>

    @GET(Constants.BASE_URL + "{movie}"+"/{movieId}")
    @Headers("accept: application/json", "content-type: application/json; charset=utf-8")
    fun getMovieDetail(
        @Path("movie", encoded = true) movie: String,
        @Path("movieId", encoded = true) movieId: String,
        @Query("api_key") api_key: String
    ): Call<MovieDetailResponse>
}
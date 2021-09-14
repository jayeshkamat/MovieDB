package com.movieDB.movies.repository.networking

import com.movieDB.movies.repository.networking.data.MovieDetailResponse
import com.movieDB.movies.repository.networking.data.MovieImagesResponse
import com.movieDB.movies.repository.networking.data.UpcomingMoviesResponse
import com.movieDB.movies.utilities.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MovieDBApi {
    private val service: MovieDBService

    companion object {

        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request = chain.request().newBuilder()
                        .method(original.method, original.body)
                        .build()
                    return chain.proceed(request)
                }
            })
            .addInterceptor(interceptor)
            .build()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(MovieDBService::class.java)
    }

    fun getUpcomingMovies(
        page: String,
        callback: Callback<UpcomingMoviesResponse>
    ) {
        service.getUpcomingMovies(
            Constants.MOVIES,
            Constants.API_KEY,
            page
        ).enqueue(callback)
    }

    fun getMovieImages(
        movieId: String,
        callback: Callback<MovieImagesResponse>
    ) {
        service.getMovieImages(
            Constants.MOVIES,
            movieId,
            Constants.API_KEY
        ).enqueue(callback)
    }

    fun getMovieDetail(
        movieId: String,
        callback: Callback<MovieDetailResponse>
    ) {
        service.getMovieDetail(
            Constants.MOVIES,
            movieId,
            Constants.API_KEY
        ).enqueue(callback)
    }
}
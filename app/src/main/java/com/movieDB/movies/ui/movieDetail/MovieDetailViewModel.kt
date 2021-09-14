package com.movieDB.movies.ui.movieDetail

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.local.db.entity.MovieDetailEntity
import com.movieDB.movies.repository.networking.MovieDBApi
import com.movieDB.movies.repository.networking.data.MovieDetailResponse
import com.movieDB.movies.repository.networking.data.MovieImagesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : AndroidViewModel(appContext) {

    private val movieDetailFetched = MutableLiveData<MovieDetailResponse>()
    val movieDetailFetchedLiveData: LiveData<MovieDetailResponse>
        get() = movieDetailFetched

    private val movieImagesFetched = MutableLiveData<MovieImagesResponse>()
    val movieImagesFetchedLiveData: LiveData<MovieImagesResponse>
        get() = movieImagesFetched

    private val movieDetailFromDB = MutableLiveData<String>()
    val movieDetailFromDBLiveData: LiveData<String>
        get() = movieDetailFromDB

    init {
        Log.i("MovieDetailVM", "MovieDetailVM created")
    }

    fun setMovieDetailResponse(apiData: MovieDetailResponse) {
        movieDetailFetched.value = apiData
    }

    fun setMovieImagesResponse(apiData: MovieImagesResponse) {
        movieImagesFetched.value = apiData
    }

    private fun setMovieDetailFromDB(data: String) {
        movieDetailFromDB.postValue(data)
    }

    fun getMovieDetail(
        movieId: String
    ) {
        repository.getMovieDetail(
            movieId,
            movieDetailResponse
        )
    }

    private val movieDetailResponse = object : Callback<MovieDetailResponse> {
        override fun onResponse(
            call: Call<MovieDetailResponse>,
            response: Response<MovieDetailResponse>
        ) {
            if (response.code() == 200 || response.code() == 201) {
                setMovieDetailResponse(response.body()!!)
            }
        }

        override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
            //error
            Log.v(" test "," error message "+t.message)
        }
    }

    fun getMovieImages(
        movieId: String
    ) {
        repository.getMovieImages(
            movieId,
            movieImagesResponse
        )
    }

    private val movieImagesResponse = object : Callback<MovieImagesResponse> {
        override fun onResponse(
            call: Call<MovieImagesResponse>,
            response: Response<MovieImagesResponse>
        ) {
            if (response.code() == 200 || response.code() == 201) {
                setMovieImagesResponse(response.body()!!)
            }
        }

        override fun onFailure(call: Call<MovieImagesResponse>, t: Throwable) {
            //error
            Log.v(" test "," error message "+t.message)
        }
    }

    fun insertMovieDetail(
        movieId: String,
        movieData: String,
        movieImages: String
    ) {
        Thread {

            //delete all items before reinserting
            db.movieDetailDao.deleteMovieDetails(movieId)

            val movieDetailEntity =
                MovieDetailEntity(movieId,movieData,movieImages)
            db.movieDetailDao.insertMovieDetails(movieDetailEntity)
        }.start()
    }

    fun getMovieDetailFromDB(
    movieId:String) {
        Thread {
            setMovieDetailFromDB(db.movieDetailDao.getMovieDetails(movieId))
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MovieDetailVM", "MovieDetailVM destroyed")
    }
}
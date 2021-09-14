package com.movieDB.movies.ui.movieListing

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.models.UpcomingMovieModel
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.local.db.entity.MovieListingEntity
import com.movieDB.movies.repository.networking.MovieDBApi
import com.movieDB.movies.repository.networking.data.UpcomingMoviesResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListingViewModel(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : AndroidViewModel(appContext) {

    private val upcomingMoviesFetched = MutableLiveData<UpcomingMoviesResponse>()
    val upcomingMoviesFetchedLiveData: LiveData<UpcomingMoviesResponse>
        get() = upcomingMoviesFetched

    private val upcomingMoviesFetchedFromDB = MutableLiveData<ArrayList<String>>()
    val upcomingMoviesFetchedFromDBLiveData: LiveData<ArrayList<String>>
        get() = upcomingMoviesFetchedFromDB

    var items = ArrayList<UpcomingMovieModel>()
    var itemsObject = ArrayList<JSONObject>()
    var currentPage = 0
    var itemsCount = 0

    init {
        Log.i("MovieListingVM", "MovieListingVM created")
    }

    fun setUpcomingMoviesResponse(apiData: UpcomingMoviesResponse) {

        //saving the data in view model.
        setMovieListing(apiData)

        //post value
        upcomingMoviesFetched.value = apiData
    }

    private fun setMovieListingFromDB(data: ArrayList<String>) {
        Log.v(" test "," set listing from db 1 ")
        upcomingMoviesFetchedFromDB.postValue(data)
    }

    fun getUpcomingMovies(
        pageNo: String
    ) {
        currentPage = pageNo.toInt()

        repository.getUpcomingMovies(
            pageNo,
            upcomingMoviesResponse
        )
    }

    private val upcomingMoviesResponse = object : Callback<UpcomingMoviesResponse> {
        override fun onResponse(
            call: Call<UpcomingMoviesResponse>,
            response: Response<UpcomingMoviesResponse>
        ) {
            if (response.code() == 200 || response.code() == 201) {
                setUpcomingMoviesResponse(response.body()!!)
            }
        }

        override fun onFailure(call: Call<UpcomingMoviesResponse>, t: Throwable) {
            //error
            Log.v(" test "," error message "+t.message)
        }
    }

    fun insertMovieListing(
        itemData: ArrayList<JSONObject>
    ) {
        Thread {

            //delete all items before reinserting all items from cart
            deleteMovieListing()

            for (i in 0 until itemData.size) {
                val movieListingEntity =
                    MovieListingEntity(itemData[i].toString())
                db.movieListingDao.insertMovieListing(movieListingEntity)
            }
        }.start()
    }

    private fun deleteMovieListing(
    ) {
        Thread {
            //delete all items before reinserting all items from cart
            db.movieListingDao.deleteMovieListing()
        }.start()
    }

    fun getMovieListingFromDB(
    ) {
        val list = ArrayList<String>()
        Thread {
            list.addAll(db.movieListingDao.getMovieListing())
            setMovieListingFromDB(list)
        }.start()

    }

    //update movie listing received from server
    private fun setMovieListing(data: UpcomingMoviesResponse) {

        itemsCount = data.total_results
        val resultArray = data.results

        //parsing response
        for (i in resultArray!!.indices) {
            val movieObject = JSONObject()
            movieObject.put(
                "id",
                resultArray[i].id
            )
            movieObject.put(
                "original_title",
                resultArray[i].original_title
            )
            movieObject.put(
                "release_date",
                resultArray[i].release_date
            )
            movieObject.put(
                "adult",
                resultArray[i].adult
            )

            if (resultArray[i].poster_path.isNullOrEmpty()) {
                movieObject.put(
                    "poster_path",
                    ""
                )
            } else {
                movieObject.put(
                    "poster_path",
                    resultArray[i].poster_path
                )
            }

            val movieModel = UpcomingMovieModel()
            movieModel.setData(movieObject)

            items.add(movieModel)
            itemsObject.add(movieObject)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MovieListingVM", "MovieListingVM destroyed")
    }
}
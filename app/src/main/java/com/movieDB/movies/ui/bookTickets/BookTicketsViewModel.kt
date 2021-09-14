package com.movieDB.movies.ui.bookTickets

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.local.db.entity.MovieTicketsEntity
import com.movieDB.movies.repository.networking.MovieDBApi

class BookTicketsViewModel(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : AndroidViewModel(appContext) {

    var cardNumber = "4242-4242-4242-4242"
    var locationOptions =
        arrayOf("Goa", "Bengaluru", "Mumbai", "Pune", "Chennai", "Hyderabad")
    var cinemaOptions =
        arrayOf("Inox", "PVR", "Central", "Inox Max")

    init {
        Log.i("BookTicketsVM", "BookTicketsVM created")
    }

    fun insertTickets(
        movieTicketData: String
    ) {
        Thread {
            val movieTicketsEntity = MovieTicketsEntity(movieTicketData)
            db.movieTicketDao.insertMovieTicketData(movieTicketsEntity)
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BookTicketsVM", "BookTicketsVM destroyed")
    }
}
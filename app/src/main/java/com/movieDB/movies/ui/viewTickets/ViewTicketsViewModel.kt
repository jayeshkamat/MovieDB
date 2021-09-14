package com.movieDB.movies.ui.viewTickets

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi

class ViewTicketsViewModel(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : AndroidViewModel(appContext) {

    private val bookedTickets = MutableLiveData<List<String>>()
    val bookedTicketsLiveData: LiveData<List<String>>
        get() = bookedTickets

    init {
        Log.i("ViewTicketsVM", "ViewTicketsVM created")
    }

    fun getTickets(
    ) {
        Thread {
            setBookedTickets(db.movieTicketDao.getTickets())
        }.start()
    }

    private fun setBookedTickets(tickets: List<String>) {
        bookedTickets.postValue(tickets)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ViewTicketsVM", "ViewTicketsVM destroyed")
    }
}
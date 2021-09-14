package com.movieDB.movies.ui.viewTickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi

class ViewTicketsViewModelFactory constructor(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ViewTicketsViewModel::class.java)) {
            ViewTicketsViewModel(appContext, this.repository, this.db) as T
        } else {
            throw IllegalArgumentException("Viewmodel not found")
        }
    }
}
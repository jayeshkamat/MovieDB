package com.movieDB.movies.ui.movieListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi

class MovieListingViewModelFactory constructor(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieListingViewModel::class.java)) {
            MovieListingViewModel(appContext, this.repository, this.db) as T
        } else {
            throw IllegalArgumentException("Viewmodel not found")
        }
    }
}
package com.movieDB.movies.ui.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi

class MovieDetailViewModelFactory constructor(
    private val appContext: MovieAppClass,
    private val repository: MovieDBApi,
    private val db: MovieDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            MovieDetailViewModel(appContext, this.repository, this.db) as T
        } else {
            throw IllegalArgumentException("Viewmodel not found")
        }
    }
}
package com.movieDB.movies.repository.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.movieDB.movies.repository.local.db.entity.MovieListingEntity
import com.movieDB.movies.repository.local.db.utils.DBConstants.Companion.TABLE_NAME_MOVIE_LISTING

@Dao
interface MovieListingDao {

    @Query("SELECT movieListingData from movieListing")
    fun getMovieListing():List<String>

    @Query("DELETE FROM $TABLE_NAME_MOVIE_LISTING")
    fun deleteMovieListing()

    @Insert
    fun insertMovieListing(movieListingEntity: MovieListingEntity)
}
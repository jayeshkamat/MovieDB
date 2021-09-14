package com.movieDB.movies.repository.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.movieDB.movies.repository.local.db.entity.MovieDetailEntity

@Dao
interface MovieDetailDao {

    @Query("SELECT movieDetailData from movieDetails WHERE movieId = :movieId")
    fun getMovieDetails(movieId:String?):String

//    @Query("SELECT movieBackdropImages from movieDetails WHERE movieId = :movieId")
//    fun getMovieImages(movieId:String)

    @Query("delete from movieDetails where movieId= :movieId")
    fun deleteMovieDetails(movieId: String?)

    @Insert
    fun insertMovieDetails(movieDetailEntity: MovieDetailEntity)
}
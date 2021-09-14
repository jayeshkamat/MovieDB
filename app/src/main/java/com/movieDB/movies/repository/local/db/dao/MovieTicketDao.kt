package com.movieDB.movies.repository.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.movieDB.movies.repository.local.db.entity.MovieTicketsEntity

@Dao
interface MovieTicketDao {

    @Query("SELECT movieTicketData from movieTickets")
    fun getTickets():List<String>

    @Insert
    fun insertMovieTicketData(movieTicketsEntity: MovieTicketsEntity)
}
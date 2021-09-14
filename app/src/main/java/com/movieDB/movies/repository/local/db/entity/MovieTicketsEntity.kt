package com.movieDB.movies.repository.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movieDB.movies.repository.local.db.utils.DBConstants

@Entity(tableName = DBConstants.TABLE_NAME_MOVIE_TICKETS)
class MovieTicketsEntity(

    @ColumnInfo(name = DBConstants.MOVIE_TICKET_DATA)
    var movieTicketData: String
) {
    @PrimaryKey(autoGenerate = true)
    var entryId: Long = 0

}

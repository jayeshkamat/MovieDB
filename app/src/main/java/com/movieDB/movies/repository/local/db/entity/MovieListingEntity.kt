package com.movieDB.movies.repository.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movieDB.movies.repository.local.db.utils.DBConstants

@Entity(tableName = DBConstants.TABLE_NAME_MOVIE_LISTING)
class MovieListingEntity(

    @ColumnInfo(name = DBConstants.MOVIE_LISTING_DATA)
    var movieListingData: String
) {
    @PrimaryKey(autoGenerate = true)
    var entryId: Long = 0

}

package com.movieDB.movies.repository.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.movieDB.movies.repository.local.db.utils.DBConstants

@Entity(tableName = DBConstants.TABLE_NAME_MOVIE_DETAILS)
class MovieDetailEntity(

    @ColumnInfo(name = DBConstants.MOVIE_ID)
    var movieId: String,
    @ColumnInfo(name = DBConstants.MOVIE_DETAIL_DATA)
    var movieDetailData: String,
    @ColumnInfo(name = DBConstants.MOVIE_BACKDROP_IMAGES)
    var movieBackdropImages: String
) {
    @PrimaryKey(autoGenerate = true)
    var entryId: Long = 0

}

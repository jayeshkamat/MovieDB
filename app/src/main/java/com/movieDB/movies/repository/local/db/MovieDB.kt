package com.movieDB.movies.repository.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.movieDB.movies.repository.local.db.dao.MovieDetailDao
import com.movieDB.movies.repository.local.db.dao.MovieListingDao
import com.movieDB.movies.repository.local.db.dao.MovieTicketDao
import com.movieDB.movies.repository.local.db.entity.MovieDetailEntity
import com.movieDB.movies.repository.local.db.entity.MovieListingEntity
import com.movieDB.movies.repository.local.db.entity.MovieTicketsEntity
import com.movieDB.movies.repository.local.db.utils.DBConstants

@Database(
    entities = [MovieDetailEntity::class, MovieListingEntity::class,MovieTicketsEntity::class],
    version = DBConstants.VERSION_CODE,
    exportSchema = false
)
abstract class MovieDB : RoomDatabase() {
    abstract val movieDetailDao: MovieDetailDao
    abstract val movieListingDao: MovieListingDao
    abstract val movieTicketDao: MovieTicketDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDB? = null

        fun getInstance(context: Context): MovieDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDB::class.java,
                        DBConstants.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
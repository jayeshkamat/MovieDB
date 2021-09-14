package com.movieDB.movies.repository.local.db.utils

class DBConstants {

    companion object {
        //table name constants
        const val TABLE_NAME_MOVIE_LISTING = "movieListing"
        const val TABLE_NAME_MOVIE_DETAILS = "movieDetails"
        const val TABLE_NAME_MOVIE_TICKETS = "movieTickets"

        //table column name constants
        const val MOVIE_ID = "movieId"
        const val MOVIE_LISTING_DATA = "movieListingData"
        const val MOVIE_DETAIL_DATA = "movieDetailData"
        const val MOVIE_BACKDROP_IMAGES = "movieBackdropImages"
        const val MOVIE_TICKET_DATA = "movieTicketData"

        //database name
        const val DB_NAME = "movieDB.db"

        //version code
        const val VERSION_CODE = 1
    }
}
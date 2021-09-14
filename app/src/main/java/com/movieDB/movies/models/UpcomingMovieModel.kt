package com.movieDB.movies.models

import org.json.JSONObject

class UpcomingMovieModel {

    var id: String? = null
    var originalTitle: String? = null
    var releaseDate: String? = null
    var adultNonAdult: Boolean? = null
    var posterPath: String? = null

    //the data is received as a JSON String
    fun setData(movieData: JSONObject) {
        id = movieData.getString("id")
        originalTitle = movieData.getString("original_title")
        releaseDate = movieData.getString("release_date")
        adultNonAdult = movieData.getBoolean("adult")
        posterPath = movieData.getString("poster_path")
    }
}
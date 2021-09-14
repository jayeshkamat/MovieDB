package com.movieDB.movies.models

import org.json.JSONObject

class BookedTicketsModel {

    var movieName: String? = null
    var location: String? = null
    var cinema: String? = null
    var seatNo: String? = null
    var cardNo: String? = null
    var bookingNumber: String? = null

    //the data is received as a JSON String
    fun setData(ticketData: JSONObject) {
        movieName = ticketData.getString("movieName")
        location = ticketData.getString("location")
        cinema = ticketData.getString("cinema")
        seatNo = ticketData.getString("seatNo")
        cardNo = ticketData.getString("cardNo")
        bookingNumber = ticketData.getString("bookingNumber")
    }
}
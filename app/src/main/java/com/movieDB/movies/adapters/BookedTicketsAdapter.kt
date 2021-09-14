package com.movieDB.movies.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movieDB.movies.R
import com.movieDB.movies.databinding.AdapterBookedTicketsBinding
import com.movieDB.movies.models.BookedTicketsModel


class BookedTicketsAdapter(
    private val context: Activity,
    list: ArrayList<BookedTicketsModel>
) : androidx.recyclerview.widget.RecyclerView.Adapter<BookedTicketsAdapter.MyViewHolder>() {

    private var listingModel = ArrayList<BookedTicketsModel>()

    inner class MyViewHolder(view: View, viewType: Int) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val binding = AdapterBookedTicketsBinding.bind(itemView)
    }

    init {
        this.listingModel = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        lateinit var itemView: View
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_booked_tickets, parent, false)

        val holder = MyViewHolder(itemView, viewType)
        return holder
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        context.runOnUiThread {

            holder.binding.movieName.text = listingModel[position].movieName
            holder.binding.cinemaLocation.text =
                "${listingModel[position].cinema}, ${listingModel[position].location}"
            holder.binding.bookingNumber.text = "B.No ${listingModel[position].bookingNumber}"
            holder.binding.cardNo.text = "C.No ${listingModel[position].cardNo}"
            holder.binding.seatNo.text = listingModel[position].seatNo
        }
    }

    override fun getItemCount(): Int {
        return listingModel.size
    }

    //function called from the parent activity or fragment to notify the adapter change
    fun setListing(list: ArrayList<BookedTicketsModel>) {

        this.listingModel = ArrayList()
        this.listingModel.addAll(list)
        notifyDataSetChanged()
    }
}
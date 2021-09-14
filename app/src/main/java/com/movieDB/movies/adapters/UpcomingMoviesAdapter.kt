package com.movieDB.movies.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.databinding.AdapterUpcomingMoviesBinding
import com.movieDB.movies.models.UpcomingMovieModel
import com.movieDB.movies.ui.movieListing.MovieListingFragment
import com.movieDB.movies.utilities.Constants
import com.movieDB.movies.utilities.PicassoTrust
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

class UpcomingMoviesAdapter(
    private val context: Context,
    list: ArrayList<UpcomingMovieModel>,
    fragment: MovieListingFragment
) : androidx.recyclerview.widget.RecyclerView.Adapter<UpcomingMoviesAdapter.MyViewHolder>() {

    private var listingModel = ArrayList<UpcomingMovieModel>()
    private val movieListingFragment = fragment

    inner class MyViewHolder(view: View, viewType: Int) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val binding = AdapterUpcomingMoviesBinding.bind(itemView)
    }

    init {
        this.listingModel = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_upcoming_movies, parent, false)

        return MyViewHolder(itemView, viewType)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        PicassoTrust.getInstance(context).cancelRequest(holder.binding.posterImage)

        holder.binding.movieName.text = listingModel[position].originalTitle
        holder.binding.movieReleaseDate.text = listingModel[position].releaseDate

        if (listingModel[position].adultNonAdult!!) {
            holder.binding.certification.text = context.resources.getString(R.string.adult)
        } else {
            holder.binding.certification.text = context.resources.getString(R.string.non_adult)
        }

        //check network status
        if (isNetworkAvailable()) {
            Picasso.with(context)
                .load(Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + listingModel[position].posterPath)
                .into(holder.binding.posterImage, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //nothing
                    }

                    override fun onError() {
                        //do nothing
                    }
                })
        } else {
            Picasso.with(context)
                .load(Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + listingModel[position].posterPath)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.binding.posterImage)
        }

        //listener to open detail activity
        holder.binding.mainLayout.setOnClickListener {
            movieListingFragment.openMovieDetail(listingModel[position].id!!)
        }

        //listener to open ticket activity
        holder.binding.bookNow.setOnClickListener {
            movieListingFragment.openTicketBooking(listingModel[position].originalTitle!!)
        }
    }

    override fun getItemCount(): Int {
        return listingModel.size
    }

    //function to detect if on app open, network online or offline
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            MovieAppClass.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //function called from the parent activity or fragment to notify the adapter change
    fun setListing(list: ArrayList<UpcomingMovieModel>) {

        this.listingModel = ArrayList()
        this.listingModel.addAll(list)
        notifyDataSetChanged()
    }
}
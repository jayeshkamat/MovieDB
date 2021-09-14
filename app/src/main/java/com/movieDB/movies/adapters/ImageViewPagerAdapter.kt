package com.movieDB.movies.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.databinding.ImageViewPagerBinding
import com.movieDB.movies.utilities.Constants
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

class ImageViewPagerAdapter(// Context object
    var context: Context, // Array of images
    var images: ArrayList<String>
) :
    PagerAdapter() {

    // Layout Inflater
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // inflating the item.xml
        val itemView: View = mLayoutInflater.inflate(R.layout.image_view_pager, container, false)
        val binding = ImageViewPagerBinding.bind(itemView)

        //check network status
        if (isNetworkAvailable()) {
            Picasso.with(context)
                .load(Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + images[position])
                .into(binding.viewPagerImageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //nothing
                    }

                    override fun onError() {
                        //do nothing
                    }
                })
        } else {
            Picasso.with(context)
                .load(Constants.BASE_IMAGE_URL + Constants.IMAGE_SIZE + images[position])
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(binding.viewPagerImageView)
        }

        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as RelativeLayout)
    }

    // Viewpager Constructor
    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    //function to detect if on app open, network online or offline
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            MovieAppClass.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
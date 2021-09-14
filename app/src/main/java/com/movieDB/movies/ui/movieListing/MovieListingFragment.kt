package com.movieDB.movies.ui.movieListing

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.adapters.UpcomingMoviesAdapter
import com.movieDB.movies.databinding.FragmentUpcomingMoviesBinding
import com.movieDB.movies.models.UpcomingMovieModel
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi
import com.movieDB.movies.repository.networking.data.UpcomingMoviesResponse
import org.json.JSONObject

private val TAG = MovieListingFragment::class.java.simpleName

class MovieListingFragment : Fragment(), View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.viewTickets -> {
                viewTickets()

//                //if tickets present, then open activity.
//                //Else display toast
//                val ticketsListing = MovieDBApplicationClass.getMovieDB().movieTicketsDao.tickets
//                if (ticketsListing.size > 0) {
//                    val openBookedTickets = Intent(this, ViewTicketsActivity::class.java)
//                    startActivity(openBookedTickets)
//                } else {
//                    Toast.makeText(
//                        this,
//                        resources.getString(R.string.no_tickets),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            }
        }
    }

    var pageNo = 1//used to append to the url during pagination
    private lateinit var upcomingMoviesAdapter: UpcomingMoviesAdapter
    var listingModel = ArrayList<UpcomingMovieModel>()
    var fetchedObjects = ArrayList<JSONObject>()
    var totalCount = 0
    var apiCallMade = 0
    private val API = MovieDBApi()
    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieListingViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)
        val view = binding.root

        //context
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDB.getInstance(application)

        //initialize view model
        viewModel = ViewModelProvider(
            this,
            MovieListingViewModelFactory(activity?.application as MovieAppClass, API, dataSource)
        ).get(MovieListingViewModel::class.java)

        //add click listener
        binding.viewTickets.setOnClickListener(this)

        //initialise adapters
        upcomingMoviesAdapter = UpcomingMoviesAdapter(
            requireContext(), listingModel, this
        )

        //on scroll listener added for the recycler view to detect when the recycler view reaches the bottom to invoke the pagination code.
        binding.listingRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {

                        if (!recyclerView.canScrollVertically(1)) {
                            //if total count > fetched count
                            //then if network available, then update page number and fetch listing.
                            if (isNetworkAvailable()) {
                                if (totalCount > fetchedObjects.size) {
                                    pageNo++
                                    binding.progressLoader.visibility = View.VISIBLE
                                    getUpcomingMovies("" + pageNo)
                                }
                            }
                        }
                    }
                }
            }
        })

        //setting the layout manager for adapter
        val mLayoutManager = LinearLayoutManager(MovieAppClass.appContext)
        binding.listingRecyclerView.layoutManager = mLayoutManager
        binding.listingRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), 0))
        binding.listingRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.listingRecyclerView.adapter = upcomingMoviesAdapter

        //on fragment recreation, if view model contains data then display from view model and set the values
        if (viewModel.items.size > 0) {
            //using last saved values from VM
            pageNo = viewModel.currentPage
            totalCount = viewModel.itemsCount

            listingModel.clear()
            listingModel.addAll(viewModel.items)
            upcomingMoviesAdapter.setListing(listingModel)

            fetchedObjects.clear()
            fetchedObjects.addAll(viewModel.itemsObject)
        } else {
            //fetch from native.
            fetchMovieListingFromDB()
        }

        //adding observers for view model
        viewModel.upcomingMoviesFetchedLiveData.observe(viewLifecycleOwner, { data ->
            setMovieListing(data)
        })

        //adding observers for view model
        viewModel.upcomingMoviesFetchedFromDBLiveData.observe(viewLifecycleOwner, { data ->
            if (viewModel.items.size > 0) {
                //ignore
            } else {
                setMovieListingFromDB(data)
            }
        })
        return view
    }

    private fun fetchMovieListingFromDB() {
        //function call to fetch upcoming movie listing
        viewModel.getMovieListingFromDB()
    }

    //function to detect if on app open, network online or offline
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            MovieAppClass.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //update movie listing received from server
    private fun setMovieListing(data: UpcomingMoviesResponse) {

        //parse the response
        totalCount = data.total_results

        val resultArray = data.results

        //parsing response
        for (i in resultArray!!.indices) {
            val movieObject = JSONObject()
            movieObject.put(
                "id",
                resultArray[i].id
            )
            movieObject.put(
                "original_title",
                resultArray[i].original_title
            )
            movieObject.put(
                "release_date",
                resultArray[i].release_date
            )
            movieObject.put(
                "adult",
                resultArray[i].adult
            )

            if (resultArray[i].poster_path.isNullOrEmpty()) {
                movieObject.put(
                    "poster_path",
                    ""
                )
            } else {
                movieObject.put(
                    "poster_path",
                    resultArray[i].poster_path
                )
            }

            val movieModel = UpcomingMovieModel()
            movieModel.setData(movieObject)

            listingModel.add(movieModel)
            fetchedObjects.add(movieObject)
        }

        //update adapter
        upcomingMoviesAdapter.setListing(listingModel)
        apiCallMade = 0
        binding.progressLoader.visibility = View.GONE

        //saving data locally
        viewModel.insertMovieListing(fetchedObjects)
    }

    //update movie listing received from db
    private fun setMovieListingFromDB(data: ArrayList<String>) {

        for (i in 0 until data.size) {
            val upcomingMovieModel = UpcomingMovieModel()
            upcomingMovieModel.setData(JSONObject(data[i]))
            listingModel.add(upcomingMovieModel)
        }

        upcomingMoviesAdapter.setListing(listingModel)

        //clearing listing model
        listingModel.clear()

        //fetch items from server
        getUpcomingMovies("" + pageNo)
    }

    private fun getUpcomingMovies(pageNo: String) {
        //fetch items from server
        viewModel.getUpcomingMovies("" + pageNo)
    }

    fun openMovieDetail(id: String) {
        val bundle = bundleOf("movieId" to id)
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.movieDetail, true)
                .build()
        view?.findNavController()?.navigate(
            R.id.action_movieListing_to_movieDetail,
            bundle,
//                navOptions
        )
    }

    fun openTicketBooking(movieName: String) {
        val bundle = bundleOf("movieName" to movieName)
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.bookTickets, true)
                .build()
        view?.findNavController()?.navigate(
            R.id.action_movieListing_to_bookTickets,
            bundle,
//                navOptions
        )
    }

    fun viewTickets() {
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.viewTickets, true)
                .build()
        view?.findNavController()?.navigate(
            R.id.action_movieListing_to_viewTickets,
            null,
//                navOptions
        )
    }
}

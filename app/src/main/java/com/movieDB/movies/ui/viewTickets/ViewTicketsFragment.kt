package com.movieDB.movies.ui.viewTickets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.adapters.BookedTicketsAdapter
import com.movieDB.movies.databinding.FragmentBookedTicketsBinding
import com.movieDB.movies.models.BookedTicketsModel
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi
import org.json.JSONObject

class ViewTicketsFragment : Fragment() {

    private var _binding: FragmentBookedTicketsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewTicketsViewModel
    private val API = MovieDBApi()
    var listingModel = ArrayList<BookedTicketsModel>()
    lateinit var bookedTicketsAdapter: BookedTicketsAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookedTicketsBinding.inflate(inflater, container, false)
        val view = binding.root

        //context
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDB.getInstance(application)

        //initialize view model
        viewModel = ViewModelProvider(
            this,
            ViewTicketsViewModelFactory(activity?.application as MovieAppClass, API, dataSource)
        ).get(ViewTicketsViewModel::class.java)

        //initialise adapters
        bookedTicketsAdapter = BookedTicketsAdapter(
            requireActivity(), listingModel
        )

        //setting the layout manager for adapter
        val mLayoutManager = LinearLayoutManager(application)
        binding.listingRecyclerView.layoutManager = mLayoutManager
        binding.listingRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), 0))
        binding.listingRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.listingRecyclerView.adapter = bookedTicketsAdapter

        //adding observers for view model
        viewModel.bookedTicketsLiveData.observe(viewLifecycleOwner, { data ->
            listingModel.clear()
            for (element in data) {
                val bookedTicketsModel = BookedTicketsModel()
                bookedTicketsModel.setData(JSONObject(element))
                listingModel.add(bookedTicketsModel)
            }
            bookedTicketsAdapter.setListing(listingModel)

            if (listingModel.size == 0) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.no_tickets),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()

        //fetch from native.
        fetchBookedTicketsFromDB()
    }

    private fun fetchBookedTicketsFromDB() {
        viewModel.getTickets()
    }
}

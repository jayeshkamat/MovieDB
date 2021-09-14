package com.movieDB.movies.ui.bookTickets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.activities.MainActivity
import com.movieDB.movies.databinding.FragmentBookTicketsBinding
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi
import org.json.JSONObject

private val TAG = BookTicketsFragment::class.java.simpleName

class BookTicketsFragment : Fragment(), View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.bookText -> {

                //save contents in db and display toast by finishing activity
                if (cinemaChosen.isEmpty() || locationChosen.isEmpty() || binding.seatNumber.text.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.enter_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val movieTicketData = JSONObject()
                    movieTicketData.put("movieName", movieName)
                    movieTicketData.put("location", locationChosen)
                    movieTicketData.put("cinema", cinemaChosen)
                    movieTicketData.put("seatNo", binding.seatNumber.text.toString())
                    movieTicketData.put("cardNo", viewModel.cardNumber)
                    movieTicketData.put("bookingNumber", "" + System.currentTimeMillis())

                    //save ticket locally
                    viewModel.insertTickets(movieTicketData.toString())

                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.tickets_booked),
                        Toast.LENGTH_SHORT
                    ).show()

                    //go back
                    (activity as MainActivity?)!!.performBackClick()
                }
            }
        }
    }

    var locationChosen = ""
    var cinemaChosen = ""
    var movieName = ""

    private var _binding: FragmentBookTicketsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookTicketsViewModel
    private val API = MovieDBApi()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookTicketsBinding.inflate(inflater, container, false)
        val view = binding.root

        //context
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDB.getInstance(application)

        //initialize view model
        viewModel = ViewModelProvider(
            this,
            BookTicketsViewModelFactory(activity?.application as MovieAppClass, API, dataSource)
        ).get(BookTicketsViewModel::class.java)

        //get movie id via intent
        val bundle = this.arguments
        movieName = bundle!!.getString("movieName", "")

        //set click listener
        binding.bookText.setOnClickListener(this)

        //item selected listeners for spinners
        binding.locationSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                locationChosen = viewModel.locationOptions[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.cinemaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                cinemaChosen = viewModel.cinemaOptions[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        //Creating the ArrayAdapter instance having the location list
        val locationAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, viewModel.locationOptions)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Creating the ArrayAdapter instance having the cinema list
        val cinemaAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, viewModel.cinemaOptions)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //setting adapters
        binding.locationSpinner.adapter = locationAdapter
        binding.cinemaSpinner.adapter = cinemaAdapter

        return view
    }
}

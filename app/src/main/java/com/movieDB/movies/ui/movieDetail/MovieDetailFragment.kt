package com.movieDB.movies.ui.movieDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.activities.PlayMovieTrailerActivity
import com.movieDB.movies.adapters.ImageViewPagerAdapter
import com.movieDB.movies.databinding.FragmentMovieDetailBinding
import com.movieDB.movies.repository.local.db.MovieDB
import com.movieDB.movies.repository.networking.MovieDBApi
import com.movieDB.movies.repository.networking.data.ImageData
import org.json.JSONArray
import org.json.JSONObject


private val TAG = MovieDetailFragment::class.java.simpleName

class MovieDetailFragment : Fragment(), View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.watchTrailer -> {
                //open video view activity
                val intent = Intent(requireActivity(), PlayMovieTrailerActivity::class.java)
                intent.putExtra("movieId",movieId)
                startActivity(intent)
            }
        }
    }

    private val API = MovieDBApi()
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter
    var imagesList = ArrayList<String>()
    var genres = JSONArray()
    var movieId = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        //context
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDB.getInstance(application)

        //initialize view model
        viewModel = ViewModelProvider(
            this,
            MovieDetailViewModelFactory(activity?.application as MovieAppClass, API, dataSource)
        ).get(MovieDetailViewModel::class.java)

        //get movie id via intent
        val bundle = this.arguments
        movieId = bundle!!.getString("movieId", "")

        //add click listener
        binding.watchTrailer.setOnClickListener(this)

        //initialise adapters
        imageViewPagerAdapter = ImageViewPagerAdapter(requireContext(), imagesList)
        binding.imageViewPager.adapter = imageViewPagerAdapter

        //fetch from native.
        fetchMovieDetailFromNative()
        getMovieImages()

        //adding observers for view model
        viewModel.movieDetailFromDBLiveData.observe(viewLifecycleOwner, { data ->
            if (data.isNullOrEmpty()) {
                //ignore
            } else {
                mapData(JSONObject(data))
            }

            getMovieDetail()
        })

        //adding observers for view model
        viewModel.movieImagesFetchedLiveData.observe(viewLifecycleOwner, { data ->
            val backdropsArray = ArrayList<ImageData>()
            if (data.backdrops!!.isNotEmpty()) {
                backdropsArray.addAll(data.backdrops)
            } else if (data.posters!!.isNotEmpty()) {
                backdropsArray.addAll(data.posters)
            }

            for (i in 0 until backdropsArray.size) {
                if (imagesList.size < 5) {
                    imagesList.add(
                        backdropsArray[i].file_path!!
                    )
                } else if (imagesList.size >= 5) {
                    break
                }
            }

            imageViewPagerAdapter.notifyDataSetChanged()
            getMovieDetail()
        })

        //adding observers for view model
        viewModel.movieDetailFetchedLiveData.observe(viewLifecycleOwner, { data ->
            //parse the response
            val imageArray = JSONArray()
            if (imagesList.size > 0) {
                for (i in 0 until imagesList.size) {
                    imageArray.put(imagesList[i])
                }
            }

            if (data.genres.isNullOrEmpty()) {
                //ignore
            } else {
                for (i in data.genres.indices) {
                    genres.put(data.genres[i].name!!)
                }
            }

            val detailObject = JSONObject()
            detailObject.put("original_title", data.original_title)
            detailObject.put("overview", data.overview)
            detailObject.put("popularity", data.popularity)
            detailObject.put("release_date", data.release_date)
            detailObject.put("genres", genres.toString())
            detailObject.put("backdrops", imageArray.toString())

            //display fetched data
            mapData(detailObject)

            //save fetched data
            viewModel.insertMovieDetail(movieId, detailObject.toString(), "")
        })

        return view
    }

    private fun fetchMovieDetailFromNative() {
        viewModel.getMovieDetailFromDB(movieId)
    }

    private fun getMovieImages() {
        viewModel.getMovieImages(movieId)
    }

    private fun getMovieDetail() {
        viewModel.getMovieDetail(movieId)
    }

    private fun mapData(detailObject: JSONObject) {

        binding.movieName.text = detailObject.getString("original_title")

        var genresString = ""
        val genresArray = JSONArray(detailObject.getString("genres"))

        for (i in 0 until genresArray.length()) {
            if (genresString.isNotEmpty()) {
                genresString = genresString + ", " + genresArray[i]
            } else {
                genresString += genresArray[i]
            }
        }
        binding.genresType.text = genresString

        binding.date.text = detailObject.getString("release_date")
        binding.overview.text = detailObject.getString("overview")

        //if images not displayed, then fetch from native if any and display
        if (imagesList.size > 0) {
            //images already displayed
        } else {
            val imageArray = JSONArray(detailObject.getString("backdrops"))
            for (i in 0 until imageArray.length()) {
                imagesList.add(imageArray[i].toString())
            }
            imageViewPagerAdapter.notifyDataSetChanged()
        }
    }
}

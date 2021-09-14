package com.movieDB.movies.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.movieDB.movies.MovieAppClass
import com.movieDB.movies.R
import com.movieDB.movies.databinding.ActivityMovieTrailerBinding
import com.movieDB.movies.utilities.Constants
import com.movieDB.movies.utilities.NetworkClass
import org.json.JSONObject
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class PlayMovieTrailerActivity : YouTubeBaseActivity() {

    var movieId = ""
    var networkCall: NetworkClass? = null
    private lateinit var binding: ActivityMovieTrailerBinding

    //observer for response of movie trailer urls
    private var movieTrailerUrlResponseObserver: Observer<String?> = object : Observer<String?> {
        override fun onCompleted() {
            Log.v(" test ", " observer complete ")
        }

        override fun onError(e: Throwable?) {
            Toast.makeText(
                MovieAppClass.appContext,
                resources.getString(R.string.no_network),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        override fun onNext(s: String?) {

            //parse the response
            if (s != null && s != "") {
                val parentObject = JSONObject(s)
                val resultsArray = parentObject.getJSONArray("results")

                if (resultsArray.length() > 0) {
                    val youTubeVideoKey = resultsArray.getJSONObject(0).getString("key")
                    playMovieTrailer(youTubeVideoKey)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieTrailerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        networkCall = NetworkClass()

        //get movie id via intent
        movieId = intent.getStringExtra("movieId").toString()

        getMovieTrailerUrl()
    }

    override fun onResume() {
        super.onResume()
        setDecorView()
    }

    fun playMovieTrailer(key: String) {
        binding.youtubePlayerView.initialize(
            Constants.YOUTUBE_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {
                    //adding event listeners
                    youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener)
                    youTubePlayer.setPlaybackEventListener(playbackEventListener)

                    //start playing video
                    youTubePlayer.loadVideo(key)
                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider?,
                    youTubeInitializationResult: YouTubeInitializationResult?
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Video player Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun getMovieTrailerUrl() {

        if (isNetworkAvailable()) {
            getTrailerUrl()!!.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieTrailerUrlResponseObserver)
        } else {
            Toast.makeText(this, resources.getString(R.string.no_network), Toast.LENGTH_SHORT).show()
        }
    }

    //function called to get movie trailer url
    private fun getTrailerUrl(): Observable<String?>? {
        return Observable.create { subscriber ->
            try {
                val response = networkCall!!.makeServiceCall(Constants.BASE_URL + Constants.MOVIES + movieId + Constants.VIDEOS + Constants.API_KEY_TAG + Constants.API_KEY)
                subscriber.onNext(response)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    //function to detect if on app open, network online or offline
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            MovieAppClass.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun setDecorView() {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

    }

    //event listeners for youtube player
    private val playbackEventListener: YouTubePlayer.PlaybackEventListener = object :
        YouTubePlayer.PlaybackEventListener {
        override fun onBuffering(arg0: Boolean) {}
        override fun onPaused() {}
        override fun onPlaying() {}
        override fun onSeekTo(arg0: Int) {}
        override fun onStopped() {}
    }

    //player state changed listeners for youtube player
    private val playerStateChangeListener: YouTubePlayer.PlayerStateChangeListener =
        object : YouTubePlayer.PlayerStateChangeListener {
            override fun onAdStarted() {}
            override fun onError(arg0: YouTubePlayer.ErrorReason?) {}
            override fun onLoaded(arg0: String?) {}
            override fun onLoading() {}
            override fun onVideoEnded() {
                finish()
            }

            override fun onVideoStarted() {}
        }
}
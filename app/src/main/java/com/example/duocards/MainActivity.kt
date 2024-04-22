package com.example.duocards

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.message.BasicHeader
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val CLIENT_ID="547f5f864cbd4056b61dbf911ffa7556"
    private val CLIENT_SECRET="e4828333ff6846c791d231e75303a1c5"
    //APP_STATUS="Development Mode"
    //APP_NAME="SpotYourTrackWeb"
    //APP_DESCRIPTION="REST API"
    //REDIRECT_URI="http://localhost:3000"

    //var accessToken: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSpotifyToken()
    }

    private fun getSpotifyToken() {
        val client = AsyncHttpClient()

        // Prepare the request parameters
        val params = RequestParams()
        params.put("grant_type", "client_credentials")
        params.put("client_id", CLIENT_ID)
        params.put("client_secret", CLIENT_SECRET)

        // Prepare the request headers if needed
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Content-Type", "application/x-www-form-urlencoded"))

        client.post(
            this@MainActivity, // Context
            "https://accounts.spotify.com/api/token", // URL
            headers.toTypedArray(), // Headers
            params, // Params
            "application/x-www-form-urlencoded", // Content type
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to get response! ${errorResponse?.toString()}")
                }
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    Log.d("Spotify API", "Success! Response: ${response?.toString()}")
                    val accessToken = response?.getString("access_token")


                    // TODO: This is where we call all the functions to use our API
                        getArtistFromAPI(accessToken)
                        getArtistTopTracksFromAPI(accessToken)

                    // TODO: make a function that grabs a random song (from an artist - might be easier)
                    // TODO: Make a loop and call that function 20 times
                    // TODO: that will be our recycler view

                }
            }
        )
    }
    private fun getArtistFromAPI(accessToken: String?) {
        if (accessToken.isNullOrEmpty()) {
            Log.d("Spotify API", "Access token is null or empty.")
            return
        }

        val client = AsyncHttpClient()

        // Prepare the request headers
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))

        client.get(
            this@MainActivity, // Context
            "https://api.spotify.com/v1/artists/6l3HvQ5sa6mXTsMTB19rO5", // URL
            headers.toTypedArray(), // Headers
            null, // Params
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to get artist details! ${errorResponse?.toString()}")
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    val artistName = response?.getString("name")
                    findViewById<TextView>(R.id.artistNameTextView).text = "Artist Name\n$artistName"
                    Log.d("Spotify API", "Success! Artist details: ${response?.toString()}")
                }
            }
        )
    }
    private fun getArtistTopTracksFromAPI(accessToken: String?) {
        if (accessToken.isNullOrEmpty()) {
            Log.d("Spotify API", "Access token is null or empty.")
            return
        }

        val client = AsyncHttpClient()

        // Prepare the request headers
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))

        client.get(
            this@MainActivity, // Context
            "https://api.spotify.com/v1/artists/6l3HvQ5sa6mXTsMTB19rO5/top-tracks?market=US", // URL
            headers.toTypedArray(), // Headers
            null, // Params
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to get artist details! ${errorResponse?.toString()}")
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    val tracksArray = response?.getJSONArray("tracks")
                    val topTracksList = ArrayList<String>()

                    // Extract track names from the response
                    if (tracksArray != null) {
                        for (i in 0 until tracksArray.length()) {
                            val track = tracksArray.getJSONObject(i)
                            val name = track.getString("name")
                            topTracksList.add(name)
                        }
                    }

                    // Pass the list to the RecyclerView adapter
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                    val adapter = ArtistAdapter(topTracksList)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    Log.d("Spotify API", "Success! Artist details: ${response?.toString()}")
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
    }

    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}
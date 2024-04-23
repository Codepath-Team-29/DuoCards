package com.example.duocards

//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.RequestParams
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.message.BasicHeader
import org.json.JSONObject
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private val CLIENT_ID="547f5f864cbd4056b61dbf911ffa7556"
    private val CLIENT_SECRET="e4828333ff6846c791d231e75303a1c5"
    //APP_STATUS="Development Mode"
    //APP_NAME="SpotYourTrackWeb"
    //APP_DESCRIPTION="REST API"
    //REDIRECT_URI="http://localhost:3000"

    //var accessToken: String? = null;
    private var mediaPlayer: MediaPlayer? = null

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
                        // getArtistFromAPI(accessToken)
                        // getArtistTopTracksFromAPI(accessToken)
                    loadArtistDetails(accessToken, "Dua Lipa")
                    // TODO: make a function that grabs a random song (from an artist - might be easier)
                        //getArtistRandomTrack(accessToken)

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
            "https://api.spotify.com/v1/artists/2BM933ADIluGGrPBOhPgIt", // URL
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
                    val artistImageArray = response?.getJSONArray("images")

                    // Check if the artistImageArray is not null and contains images
                    if (artistImageArray != null && artistImageArray.length() > 0) {
                        val image = artistImageArray.getJSONObject(0)
                        val imageUrl = image.getString("url")

                        // Load image into ImageView using Picasso
                        val imageView = findViewById<ImageView>(R.id.imageView)
                        Picasso.get().load(imageUrl).into(imageView)
                    }

                    findViewById<TextView>(R.id.artistNameTextView).text = "Artist Name\n$artistName"
                    Log.d("Spotify API", "Success! Artist details: ${response?.toString()}")

                }
            }
        )
    }

    private fun searchArtistIdByName(accessToken: String?, artistName: String, callback: (String?) -> Unit) {
        if (accessToken.isNullOrEmpty()) {
            Log.d("Spotify API", "Access token is null or empty.")
            callback(null) // Notify the caller of the failure
            return
        }

        val client = AsyncHttpClient()

        // Prepare the request headers
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))

        val query = URLEncoder.encode(artistName, "UTF-8")
        val url = "https://api.spotify.com/v1/search?q=$query&type=artist"

        client.get(
            this@MainActivity, // Context
            url, // Search API endpoint
            headers.toTypedArray(), // Headers
            null, // Params
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to search artist ID! ${errorResponse?.toString()}")
                    callback(null) // Notify the caller of the failure
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    val artistsArray = response?.getJSONObject("artists")?.getJSONArray("items")
                    if (artistsArray != null && artistsArray.length() > 0) {
                        val artist = artistsArray.getJSONObject(0) // Take the first artist from search results
                        val artistId = artist.getString("id")
                        val artistName = artist.getString("name")

                        // Do something with the artist ID and name
                        Log.d("Spotify API", "Artist ID: $artistId, Artist Name: $artistName")

                        // Notify the caller with the artist ID
                        callback(artistId)
                    } else {
                        Log.d("Spotify API", "No artist found with the name: $artistName")
                        callback(null) // Notify the caller of the failure
                    }
                }
            }
        )
    }

//    private fun getArtistTopTracksFromAPI(accessToken: String?) {
//        if (accessToken.isNullOrEmpty()) {
//            Log.d("Spotify API", "Access token is null or empty.")
//            return
//        }
//
//        val client = AsyncHttpClient()
//
//        // Prepare the request headers
//        val headers = ArrayList<Header>()
//        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))
//
//        client.get(
//            this@MainActivity, // Context
////            "https://api.spotify.com/v1/artists/6l3HvQ5sa6mXTsMTB19rO5/top-tracks?market=US", // URL
////            "https://api.spotify.com/v1/artists/06HL4z0CvFAxyc27GXpf02/top-tracks?market=US", // Taylor Swift
//
//            "https://api.spotify.com/v1/artists/2BM933ADIluGGrPBOhPgIt/top-tracks?market=US",
//            headers.toTypedArray(), // Headers
//            null, // Params
//            object : JsonHttpResponseHandler() {
//                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
//                    // Handle failure
//                    Log.d("Spotify API", "Failure to get artist details! ${errorResponse?.toString()}")
//                }
//
//                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
//                    // Handle success
//                    val tracksArray = response?.getJSONArray("tracks")
//                    val topTracksList = ArrayList<String>()
//                    val topURL = ArrayList<String>()
//
//                    // Extract track names from the response
//                    if (tracksArray != null) {
//                        for (i in 0 until tracksArray.length()) {
//                            val track = tracksArray.getJSONObject(i)
//                            val name = track.getString("name")
//                            val url = track.getString("preview_url")
//                            topTracksList.add(name)
//                            topURL.add(url)
//                        }
//                    }
//
//                    // Pass the list to the RecyclerView adapter
//                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//                    val adapter = ArtistAdapter(topTracksList, topURL) { previewUrl -> playTrack(previewUrl) }
//                    recyclerView.adapter = adapter
//                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
//
//                    Log.d("Spotify API", "Success! Artist details: ${response?.toString()}")
//                }
//            }
//        )
//    }

    private fun getArtistTopTracksFromAPI(accessToken: String?, artistId: String) {
        if (accessToken.isNullOrEmpty()) {
            Log.d("Spotify API", "Access token is null or empty.")
            return
        }

        val client = AsyncHttpClient()

        // Prepare the request headers
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))

        val url = "https://api.spotify.com/v1/artists/$artistId/top-tracks?market=US"

        client.get(
            this@MainActivity, // Context
            url, // Top tracks API endpoint
            headers.toTypedArray(), // Headers
            null, // Params
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to get artist top tracks! ${errorResponse?.toString()}")
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    if (response != null) {
                        val tracksArray = response.optJSONArray("tracks")
                        val topTracksList = ArrayList<String>()
                        val topURL = ArrayList<String>()

                        // Extract track names and URLs from the response
                        if (tracksArray != null) {
                            for (i in 0 until tracksArray.length()) {
                                val track = tracksArray.getJSONObject(i)
                                val name = track.optString("name")
                                val url = track.optString("preview_url")
                                if (!name.isNullOrEmpty() && !url.isNullOrEmpty()) {
                                    topTracksList.add(name)
                                    topURL.add(url)
                                }
                            }
                        }

                        // Pass the lists to the RecyclerView adapter
                        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                        val adapter = ArtistAdapter(topTracksList, topURL) { previewUrl -> playTrack(previewUrl) }
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                        Log.d("Spotify API", "Success! Artist details: ${response.toString()}")
                    } else {
                        Log.d("Spotify API", "Empty response or missing 'tracks' field.")
                    }
                }

            }
        )
    }

    private fun loadArtistDetails(accessToken: String?, artistName: String) {
        // Search for artist ID by name
        searchArtistIdByName(accessToken, artistName) { artistId ->
            if (artistId != null) {
                // If artist ID is found, fetch top tracks and artist details
                getArtistTopTracksFromAPI(accessToken, artistId)
                // Fetch artist details including image
                fetchArtistDetails(accessToken, artistId)
            } else {
                Log.d("Spotify API", "Failed to get artist ID for $artistName")
            }
        }
    }

    private fun fetchArtistDetails(accessToken: String?, artistId: String) {
        if (accessToken.isNullOrEmpty()) {
            Log.d("Spotify API", "Access token is null or empty.")
            return
        }

        val client = AsyncHttpClient()

        // Prepare the request headers
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Authorization", "Bearer $accessToken"))

        val url = "https://api.spotify.com/v1/artists/$artistId"

        client.get(
            this@MainActivity, // Context
            url, // Artist details API endpoint
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
                    val artistImageArray = response?.getJSONArray("images")

                    // Check if the artistImageArray is not null and contains images
                    if (artistImageArray != null && artistImageArray.length() > 0) {
                        val image = artistImageArray.getJSONObject(0)
                        val imageUrl = image.getString("url")

                        // Load image into ImageView using Picasso
                        val imageView = findViewById<ImageView>(R.id.imageView)
                        Picasso.get().load(imageUrl).into(imageView)
                    }

                    findViewById<TextView>(R.id.artistNameTextView).text = "$artistName"
                    findViewById<TextView>(R.id.artistNameTextView).setTextColor(Color.WHITE)

                    Log.d("Spotify API", "Success! Artist details: ${response?.toString()}")
                }
            }
        )
    }


    private fun playTrack(previewUrl: String?) {
        if (previewUrl.isNullOrEmpty()) {
            Log.e("MediaPlayer", "Error: Preview URL is null or empty")
            return
        }

        try {
            stopTrack()
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer?.setDataSource(previewUrl)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error playing track: ${e.message}")
        }
    }

    private fun stopTrack() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            reset()
        }
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
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSpotifyAPI()
    }

    private fun testSpotifyAPI() {
        val client = AsyncHttpClient()

        // Prepare the request parameters
        val params = JSONObject()
        params.put("grant_type", "client_credentials")
        params.put("client_id", CLIENT_ID)
        params.put("client_secret", CLIENT_SECRET)

        // Prepare the request headers if needed
        val headers = ArrayList<Header>()
        headers.add(BasicHeader("Content-Type", "application/x-www-form-urlencoded"))

        // Prepare the request body
        val entity = StringEntity(params.toString())

        client.post(
            this@MainActivity, // Context
            "https://accounts.spotify.com/api/token", // URL
            headers.toTypedArray(), // Headers
            params, // Body
            "application/json", // Content type
            object : JsonHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                    // Handle failure
                    Log.d("Spotify API", "Failure to get response! $errorResponse")
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    // Handle success
                    Log.d("Spotify API", "Success! Response: $response")
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
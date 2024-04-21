package com.example.duocards

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE

class MainActivity : AppCompatActivity() {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val APP_NAME="SpotYourTrack"
    private val CLIENT_ID="97e4b075aa1049669d98647b990b6ec8"
    private val APP_STATUS="Development Mode"
    private val REDIRECT_URI="spotify-android-quick-start://spotify-login-callback"

    private val SCOPES = arrayOf(
        "user-read-recently-played", "user-library-read", "playlist-read-private"
    ) // Adjust scopes as needed

    val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        .setScopes(SCOPES)
    val request = builder.build()
    // AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connect to spotify api when button is clicked
        val connectButton = findViewById<Button>(R.id.connectButton)
        connectButton.setOnClickListener {
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        }

    }


    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@MainActivity.spotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")

                    connected()
                }

                override fun onFailure(error: Throwable) {
                    // Handle connection failure
                    Log.d("MainActivity", "Error connecting!", error)
                }
            })
    }

    private fun connected() {
        // Subscribe to PlayerState
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()
            ?.setEventCallback { playerState ->
                val track: Track? = playerState.track
                if (track != null) {
                    // Handle the track
                }
            }
        Log.d("MainActivity", "Connected!")
    }


    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }
}
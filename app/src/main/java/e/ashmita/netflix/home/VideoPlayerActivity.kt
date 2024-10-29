package e.ashmita.netflix.home

import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val url = intent.getStringExtra("url")
        //initialize exoplayer
        exoPlayer = ExoPlayer.Builder(this).build()

        binding.video.player = exoPlayer
        val videoUri = Uri.parse(url)

        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

    }

companion object{

}
    @OptIn(UnstableApi::class)
    fun pipMode() {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                android.os.Process.myUid(), packageName
            ) == AppOpsManager.MODE_ALLOWED
        } else {
            false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (status) {
                this.enterPictureInPictureMode(PictureInPictureParams.Builder().build())
                binding.video.hideController()
                exoPlayer.play()
            } else {
                val intent = Intent(
                    "android.settings.PICTURE_IN_PICTURE_SETTINGS",
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Feature not supported", Toast.LENGTH_SHORT).show()
            exoPlayer.play()
        }


    }

    override fun onStart() {
        super.onStart()
        exoPlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.playWhenReady = false
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the player when done
        exoPlayer.release()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        pipMode()
    }
}
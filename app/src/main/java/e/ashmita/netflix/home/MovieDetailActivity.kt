package e.ashmita.netflix.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.Picasso
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.ActivityMovieDetailBinding
import e.ashmita.netflix.model.Movie
import java.io.File
import java.util.Objects


class MovieDetailActivity : AppCompatActivity() {

    var movie : Movie? = null

    private lateinit var binding: ActivityMovieDetailBinding
//    private val authorites = "$e.mishmash.netflix.file provider"

    companion object {
        val PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        lateinit var MOVIE: String

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         movie = intent?.getSerializableExtra("movie") as Movie
        MOVIE = movie?.videoUrl.toString()

        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_NOT_EXPORTED)

        setData(movie!!)

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        binding.playBtn.setOnClickListener {
            val url = movie?.videoUrl
            if (url != null) {
                goToVideoPlayer(url)
            }
        }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        binding.downloadBtn.setOnClickListener {
            showAlertDialogToRequestPermission()

        }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        binding.shareBtn.setOnClickListener {
            shareFile()
//            val shareIntent = Intent()
//            //set action means ye action batayega ki ye intent krta kya h
//            shareIntent.action = Intent.ACTION_SEND
//            //kis type ki intent share ho rha h == * se phle type of file batate h agar video hoti ti video/* likhte
//            shareIntent.type = "video/*"
//            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(movie?.videoUrl))
//            startActivity(Intent.createChooser(shareIntent, "Sharing Music File"))
        }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun shareFile() {
        val videoUrl = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), movie?.movieName+".mp4")
//
////        val path = FileProvider.getUriForFile(this,)
//        if(videoUrl.exists())`
//        {
//            val videoUri = FileProvider.getUriForFile(
//                this,
//                "${applicationContext.packageName}.fileprovider",
//                videoUrl
//            )
//            shareVideo(videoUri)
//        }
//        else {
//            Toast.makeText(this, "Video file does not exist!", Toast.LENGTH_SHORT).show()
//        }

        val file = FileProvider.getUriForFile(applicationContext, "e.ashmita.netflix" + ".provider", File(videoUrl.absolutePath))
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.setType("*/*")
//        intent.putExtra(Intent.EXTRA_STREAM, file)
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent)


        ShareCompat.IntentBuilder.from(this)
            .setStream(file)
            .setType("video/*")
            .startChooser()
    }

    private fun shareVideo(videoUri: Uri?) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "video/*"
            putExtra(Intent.EXTRA_STREAM, videoUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share video via"))
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun showAlertDialogToRequestPermission() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required to Download")
            .setMessage("Do you want to download video?")
            .setPositiveButton("Yes") { dialog, _ ->
                requestRuntimePermission()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                Toast.makeText(this, "Permission required to download video", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            val videoUrl = MOVIE
            downloadVideo(videoUrl)
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
            Toast.makeText(this, "Permission needed", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION), 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val videoUrl = MOVIE
                downloadVideo(videoUrl)
            }
        } else {
            Toast.makeText(this, "download failed...", Toast.LENGTH_SHORT).show()
        }

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun setData(movie: Movie) {
        Picasso.get().load(movie.movieImage).into(binding.movieImage)
        binding.movieName.text = movie.movieName
        binding.language.text = movie.language
        binding.developer.text = movie.developer
        binding.category.text = movie.movieCategory
        binding.releaseYear.text = movie.releaseYear
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun goToVideoPlayer(url: String) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun downloadVideo(url: String) {
        try {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Video Download")
                .setDescription("Downloading video...")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                     movie?.movieName+ ".mp4")
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            Toast.makeText(this, "Download Failed", Toast.LENGTH_SHORT).show()
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
            binding.downloadCompleteBtn.visibility = View.VISIBLE
            binding.downloadBtn.visibility = View.GONE
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
}

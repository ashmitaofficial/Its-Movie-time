//package e.ashmita.netflix
//
//import android.app.AppOpsManager
//import android.app.PictureInPictureParams
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.widget.Toast
//import androidx.core.content.ContextCompat.getSystemService
//
//object pipmode {
//
//
//    fun pipMode() {
//        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
//        val status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            appOps.checkOpNoThrow(
//                AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
//                android.os.Process.myUid(), packageName
//            ) == AppOpsManager.MODE_ALLOWED
//        } else {
//            false
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (status) {
//                this.enterPictureInPictureMode(PictureInPictureParams.Builder().build())
//                binding.video.hideController()
//                exoPlayer.play()
//            } else {
//                val intent = Intent(
//                    "android.settings.PICTURE_IN_PICTURE_SETTINGS",
//                    Uri.parse("package:$packageName")
//                )
//                startActivity(intent)
//            }
//        } else {
//            Toast.makeText(this, "Feature not supported", Toast.LENGTH_SHORT).show()
//            exoPlayer.play()
//        }
//
//    }
//}
package e.ashmita.netflix.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import e.ashmita.netflix.R
import e.ashmita.netflix.authentication.AuthenticationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

//        CoroutineScope(Dispatchers.Main).launch {
//            delay(3000)
//            val authenticationIntent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
//            startActivity(authenticationIntent)
//            finish()
//        }

    }

    override fun onStart() {
        super.onStart()
        //current firebase user id
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                val i = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            }
        } else
        {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                val i = Intent(this@SplashActivity, AuthenticationActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

}
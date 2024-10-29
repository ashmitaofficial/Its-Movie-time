package e.ashmita.netflix.home

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.ActivityHomeBinding
import e.ashmita.netflix.search.SaearchFragment

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.bottomNav.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                if (p0.itemId == R.id.home_txt) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment::class.java, null)
                        .commit()
                    return true
                } else if (p0.itemId == R.id.search_txt) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SaearchFragment::class.java, null)
                        .commit()
                    return true
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MyAccountFragment::class.java, null)
                        .commit()
                    return true
                }
                return false
            }
        })
        binding.bottomNav.selectedItemId = R.id.home_txt

    }

}

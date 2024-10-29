package e.ashmita.netflix.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import e.ashmita.netflix.R
import e.ashmita.netflix.adapter.BannerViewPagerAdapter
import e.ashmita.netflix.adapter.LabelAndMovieAdapter
import e.ashmita.netflix.databinding.ActivityHomeBinding
import e.ashmita.netflix.databinding.FragmentHomeBinding
import e.ashmita.netflix.model.DataResponse
import e.ashmita.netflix.model.Movie

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val imageHandler = Handler(Looper.getMainLooper())
    var reference: DatabaseReference? = null
    var referenceData: DatabaseReference? = null
    var bannerList = ArrayList<Movie?>()
    var movieList = ArrayList<Movie>()
    var list = ArrayList<DataResponse>()

    private var currentPage = 0
    private val imageSwitchRunnable = object : Runnable {
        override fun run() {
            // Increase the page position by 1
            currentPage++

            // If we reach the end of the list, start over from the first page
            if (currentPage >= (binding.viewPager.adapter?.itemCount ?: 0)) {
                currentPage = 0
            }

            // Set the current item
            binding.viewPager.setCurrentItem(currentPage, true)

            // Re-run the handler after 2 seconds
            imageHandler.postDelayed(this, 2000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpViewPager()


        //FOR BANNER
        reference = FirebaseDatabase.getInstance().reference.child("banners")

        reference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    val banner: Movie? = snapshot.getValue(Movie::class.java)
                    bannerList.add(banner)
                }

                binding.viewPager.adapter =
                    BannerViewPagerAdapter(bannerList = bannerList, ctx = requireActivity())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
        //FOR DATA
        referenceData = FirebaseDatabase.getInstance().reference.child("Data")

        referenceData?.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    val label = snapshot.child("label").value
                    val movies = snapshot.child("movie")
                    for (movie in movies.children) {
                        if (movie.exists()) {
                            val m = movie.getValue(Movie::class.java)
                            movieList.add(m!!)
                        }
                    }

                    val list2 = ArrayList<Movie>(movieList.size)
                    list2.addAll(movieList)
                    list.add(DataResponse(label = label, movie = list2))
                    movieList.clear()
                }
                binding.myRV.adapter = LabelAndMovieAdapter(list, requireActivity())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //---------------------------------------------------------------------------------------------------------------------//




        return binding.root
    }

    private fun setUpViewPager() {
        //set the orientation of the viewpager using ViewPager2.orientation
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //select any page you want as your starting page
        val currentPageIndex = 1
        binding.viewPager.currentItem = currentPageIndex

        // registering for page change callback
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            }
        )

        imageHandler.postDelayed(imageSwitchRunnable, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()

        // unregistering the onPageChangedCallback
        binding.viewPager.unregisterOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {}
        )

        imageHandler.removeCallbacks(imageSwitchRunnable)
    }


}
package e.ashmita.netflix.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import e.ashmita.netflix.R
import e.ashmita.netflix.adapter.MovieAdapter
import e.ashmita.netflix.databinding.FragmentSaearchBinding
import e.ashmita.netflix.model.Movie


class SaearchFragment : Fragment() {

    lateinit var binding: FragmentSaearchBinding
    var referenceData: DatabaseReference? = null
    var movieList = ArrayList<Movie>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSaearchBinding.inflate(layoutInflater, container, false)

        binding.movieListRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)

//         val labelName = intent?.getSerializableExtra("label_name")

        referenceData = FirebaseDatabase.getInstance().reference.child("Movies")

        referenceData?.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(p0: DataSnapshot) {
                for (snapshot in p0.children) {
                    val movie: Movie? = snapshot.getValue(Movie::class.java)
                    if (movie != null) {
                        movieList.add(movie)
                    }
                }

                binding.movieListRecyclerView.adapter =
                    MovieAdapter(movieList,requireActivity())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




        return binding.root
    }

}
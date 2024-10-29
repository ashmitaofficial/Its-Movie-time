package e.ashmita.netflix.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.VideoItemBinding
import e.ashmita.netflix.home.MovieDetailActivity
import e.ashmita.netflix.home.VideoPlayerActivity
import e.ashmita.netflix.model.Movie
import java.io.Serializable

class MovieAdapter(var list:ArrayList<Movie>, val context: Context) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(list[position].movieImage)
//        holder.binding.movieName.text = list[position].movieName
        holder.binding.playBtn.setOnClickListener {
            val movieIntent = Intent(context, MovieDetailActivity::class.java)
//            VideoPlayerActivity.pipStatus = 1
            movieIntent.putExtra("movie", list[position] as Serializable)
            context.startActivity(movieIntent)
        }
    }

    class MyViewHolder(val binding: VideoItemBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(imageUrl: String?) {
            Picasso.get()
                .load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .fit()
                // Optional: if you want Picasso to resize the image to fit the ImageView
                .into(binding.videoImage);

        }
    }
}
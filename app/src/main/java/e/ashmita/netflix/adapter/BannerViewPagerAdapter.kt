package e.ashmita.netflix.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import e.ashmita.netflix.home.MovieDetailActivity
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.BannerItemBinding
import e.ashmita.netflix.home.VideoPlayerActivity
import e.ashmita.netflix.model.Movie
import java.io.Serializable

class BannerViewPagerAdapter(val ctx: Context, private val bannerList: ArrayList<Movie?>) :
    RecyclerView.Adapter<BannerViewPagerAdapter.ViewPagerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerViewPagerAdapter.ViewPagerViewHolder {
        val binding = BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BannerViewPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.setData(bannerList.get(position)?.movieImage)
        holder.itemView.setOnClickListener {
            val movieIntent = Intent(ctx, MovieDetailActivity::class.java)
//            VideoPlayerActivity.pipStatus = 2
            movieIntent.putExtra("movie", bannerList[position] as Serializable)
            ctx.startActivity(movieIntent)

        }
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }


    inner class ViewPagerViewHolder(val binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(imageUrl: String?) {
            Picasso.get()
                .load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .fit()
                // Optional: if you want Picasso to resize the image to fit the ImageView
                .into(binding.ivImage);

        }

    }
}
package e.ashmita.netflix.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import e.ashmita.netflix.databinding.LabelMovieItemBinding
import e.ashmita.netflix.model.DataResponse
import e.ashmita.netflix.model.Movie
import java.io.Serializable

class LabelAndMovieAdapter(var list :ArrayList<DataResponse>, val context: Context) : RecyclerView.Adapter<LabelAndMovieAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LabelMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.label.text = list[position].label.toString()
        holder.recyclerView.adapter = list[position].movie?.let { MovieAdapter(it,context) }


    }

//    fun updateMusicList(searchList: ArrayList<Movie>) {
//        list = ArrayList()
//        list.addAll(searchList)
//        notifyDataSetChanged()
//    }

    class MyViewHolder(binding: LabelMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val label  = binding.category
        val recyclerView = binding.RVMovie

    }

}
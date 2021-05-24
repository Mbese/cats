package com.example.mycats.cats.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycats.R
import com.example.mycats.cats.data.Cat

class CatsAdapter(
        private val context: Context,
        private val cats: ArrayList<Cat>,
        private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<CatsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rootView = getLayoutInflater(parent.context).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val url = cats[position].url
        Glide.with(context).load(url).into(holder.thumbnail)
        holder.thumbnail.setOnClickListener { itemClickListener.onItemClicked(url) }
    }

    @VisibleForTesting
    internal fun getLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    interface ItemClickListener {
        fun onItemClicked(url: String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
    }
}
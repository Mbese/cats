package com.example.mycats.cats.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cat")
data class Cat(
        @Embedded val breeds: ArrayList<Breeds>,
        @Embedded val categories: ArrayList<Categories>,
        @PrimaryKey
        @field:SerializedName("id") val id: String,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("width") val width: Int,
        @field:SerializedName("height") val height: Int
)

@BindingAdapter("fullImage")
fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().centerInside())
                .into(view)
}

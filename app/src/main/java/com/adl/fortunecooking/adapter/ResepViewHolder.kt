package com.adl.fortunecooking.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.DetailResepActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.card_holder.view.*

class ResepViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameResep = view.txtResepName
    val imageResep = view.imgResep
    val navigasi = view.cardResep
    val rating = view.ratingBar


    fun bindData(adapter:ResepAdapter, position:Int){


        nameResep.setText(adapter.data.get(position)?.title.toString())
        imageResep?.let {
            Glide.with(adapter.parent.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .load(adapter.data.get(position).ImageUri)
                .into(it)
        }
        rating.setRating(adapter.data.get(position)?.rating.toFloat())

        navigasi.setOnClickListener{
            val intent = Intent(adapter.parent.context, DetailResepActivity::class.java)
            intent.putExtra("data",adapter.data.get(position))
            adapter.parent.context.startActivity(intent)

        }


    }
}
package com.adl.fortunecooking.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.card_holder.view.*

class ResepViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameResep = view.txtResepName
    val imageResep = view.imgResep



    fun bindData(adapter:ResepAdapter, position:Int){


        nameResep.setText(adapter.data.get(position)?.nama.toString())
        imageResep?.let {
            Glide.with(adapter.parent.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .load(adapter.data.get(position).gambar)
                .into(it)
        }

    }
}
package com.adl.fortunecooking.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.DetailResepActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.card_holder.view.*
import kotlinx.android.synthetic.main.card_search_item.view.*

class SearchViewHolder (view: View): RecyclerView.ViewHolder(view){
    val nameResep = view.txtResepNameSearch
    val imageResep = view.imgResepSearch
    val navigasi = view.cardSearch
    val rating = view.ratingBarSearch
    val desc = view.txtDescriptionSearch


    fun bindData(adapter: SearchAdapter, position:Int){


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
        desc.setText(adapter.data.get(position).Deskripsi)


    }
}
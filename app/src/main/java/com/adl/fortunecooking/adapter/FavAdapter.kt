package com.adl.fortunecooking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.ResepModel

class FavAdapter(val data: ArrayList<ResepModel>): RecyclerView.Adapter<FavViewHolder>() {

    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        this.parent = parent

        return FavViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_favorite_holder,parent,false))
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bindData(this@FavAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
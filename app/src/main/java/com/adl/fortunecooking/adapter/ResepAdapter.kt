package com.adl.fortunecooking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.ResepModel

class ResepAdapter (val data: ArrayList<ResepModel>): RecyclerView.Adapter<ResepViewHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        this.parent = parent

        return ResepViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_holder,parent,false))
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        holder.bindData(this@ResepAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
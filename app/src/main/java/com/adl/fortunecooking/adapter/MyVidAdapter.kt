package com.adl.fortunecooking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.ResepModel

class MyVidAdapter(val data: ArrayList<ResepModel>): RecyclerView.Adapter<MyVidViewHolder>() {

    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVidViewHolder {
        this.parent = parent

        return MyVidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_my_videos,parent,false))
    }

    override fun onBindViewHolder(holder: MyVidViewHolder, position: Int) {
        holder.bindData(this@MyVidAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
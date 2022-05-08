package com.adl.fortunecooking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.ResepModel

class SearchAdapter (val data: ArrayList<ResepModel>): RecyclerView.Adapter<SearchViewHolder>(){
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        this.parent = parent

        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_search_item,parent,false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(this@SearchAdapter,position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
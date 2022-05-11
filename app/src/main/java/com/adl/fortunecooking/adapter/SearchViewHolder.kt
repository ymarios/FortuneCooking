package com.adl.fortunecooking.adapter

import android.content.Intent
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.DetailResepActivity
import com.adl.fortunecooking.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_resep.*
import kotlinx.android.synthetic.main.card_holder.view.*
import kotlinx.android.synthetic.main.card_search_item.view.*

class SearchViewHolder (view: View): RecyclerView.ViewHolder(view){
    private var isInMyFavorite=false
    private lateinit var firebaseAuth: FirebaseAuth

    val nameResep = view.txtResepNameSearch
    val imageResep = view.imgResepSearch
    val navigasi = view.cardSearch
    val rating = view.ratingBarSearch
    val desc = view.txtDescriptionSearch
    val favorit = view.btnAddFavHolder



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
        checkIsFavorite(adapter.data.get(position).id)


    }


    private fun checkIsFavorite(videosId: String){
        firebaseAuth = FirebaseAuth.getInstance()
        Log.d(ControlsProviderService.TAG, "checkIsFavorite: Checking if recipe is in fav or not")

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavorite = snapshot.exists()
                    Log.d("Status", "onDataChange: ${snapshot.exists()}")
                    if (isInMyFavorite){
                        //available in favorite
                        Log.d(ControlsProviderService.TAG, "onDataChange: available in favorite")
                        //set drawable top icon
                        favorit.setImageResource(R.drawable.ic_savedwatch)
                      //  btn_add_favorite.setText("My Favorite")
                    }
                    else{
                        //not available in favorite
                        Log.d(ControlsProviderService.TAG, "onDataChange: not available in favorite")
                        //set drawable top icon
                        favorit.setImageResource(R.drawable.ic_unsavedwatch)
                       // btn_add_favorite.setText("Add Favorite")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
package com.adl.fortunecooking.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.DetailResepActivity
import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.card_holder.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ResepViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameResep = view.txtResepName
    val imageResep = view.imgResep
    val navigasi = view.cardResep
    val rating = view.ratingBar
    val username_dispaly = view.username_display
    lateinit var database: DatabaseReference


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

//        getUser(adapter.data.get(position).uid)

    }

    fun getUser(userId:String){
//        database= FirebaseDatabase.getInstance().reference.child("Videos").child("${userId}")
//        database.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//
//            } else {
//                Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
//            }
//        }
        val rootRef = FirebaseDatabase.getInstance().reference
        val usersRef = rootRef.child("Users")
        val okQuery = usersRef.orderByChild("uid").equalTo("${userId}")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if(ds.exists()){
                        val username = ds.child("name").getValue(String::class.java)!!.toFloat()
                        username_dispaly.setText("${username}")
                    }

                    Log.d("isi","${ds.child("ratingValue").getValue(String::class.java)}")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        okQuery.addListenerForSingleValueEvent(valueEventListener)

    }
}
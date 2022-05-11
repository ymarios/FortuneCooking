package com.adl.fortunecooking.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adl.fortunecooking.DetailResepActivity
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_resep.*
import kotlinx.android.synthetic.main.card_holder.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ResepViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private var isInMyFavorite=false
    private lateinit var firebaseAuth: FirebaseAuth
    val nameResep = view.txtResepName
    val imageResep = view.imgResep
    val navigasi = view.cardResep
    val rating = view.ratingBar
    val username_dispaly = view.username_display
    val favorit = view.btnAddFavHome
    val addFav = view.btnAddFavHome
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
        checkIsFavorite(adapter.data.get(position)?.id)

        addFav.setOnClickListener({
            if (isInMyFavorite){
                // already in fav, remove
                removeFromFavorite(adapter.data.get(position)?.id)
            }
            else{
                //not in fav, add
                addToFavorite(adapter.data.get(position)?.id)
            }
        })

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


    private fun addToFavorite(videosId:String){
        Log.d(ControlsProviderService.TAG, "addToFavorite: Adding to fav")
        val timestamp = System.currentTimeMillis()

        //setup data to add in db
        val hashMap = HashMap<String, Any>()
        hashMap["videosId"] = videosId
        hashMap["timestamp"] = "${timestamp}"

        //save to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .setValue(hashMap)
            .addOnSuccessListener {
                //added to fav
                Log.d(ControlsProviderService.TAG, "addToFavorite: Added to fav")
            }
            .addOnFailureListener { e->
                //failed to add to fav
                Log.d(ControlsProviderService.TAG, "addToFavorite: Failed to add to fav due to ${e.message}")
              //  Toast.makeText(this,"Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFavorite(videosId:String){
        Log.d(ControlsProviderService.TAG, "removeFromFavorite: Removing from fav")

        //database ref
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(ControlsProviderService.TAG, "removeFromFavorite: Removed from fav")
             //   Toast.makeText(this,"Removed from fav", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Log.d(ControlsProviderService.TAG, "removeFromFavorite: Failed to remove from fave due to ${e.message}")
             //   Toast.makeText(this,"Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
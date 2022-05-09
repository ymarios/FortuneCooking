package com.adl.fortunecooking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adl.fortunecooking.fragments.RatingFragment

import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_detail_resep.*

class DetailResepActivity : AppCompatActivity() {

    lateinit var data:ResepModel

//    private var recipeId = ""

//    //will hold a boolean value false/true to indicate either is in current user's favorite list or not
//    private var isInMyFavorite = false

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)
        if(intent.hasExtra("data")) {
            data = intent.getParcelableExtra("data")!!
            //isUpdate = true
            //setUIWithModel(data)
            imgFoodDetail?.let {
                Glide.with(this)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .load(data.ImageUri)
                    .into(it)
            }
            txtNameDetail.setText(data.title)
            txtDescriptionDetail.setText(data.Deskripsi)
            txtResepDetail.setText(data.Resep)
            txtStepDetail.setText(data.Step)
            rateView.setText("${data.rating.toFloat()}")


        }
        btnPlayVideo.setOnClickListener({
            val intent = Intent(this@DetailResepActivity, PlayVideo::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        })

        btn_back_home.setOnClickListener({
            var arguments = Bundle()
            arguments.putParcelable("data",data);
            var myFragment: Fragment = RatingFragment()
            myFragment.setArguments(arguments);
            Log.d("args",arguments.toString())
            showDialog()
//            finish()
//            onBackPressed()
        })



    }
    fun showDialog() {
        val fragmentManager = supportFragmentManager

        var arguments = Bundle()
        arguments.putParcelable("data",data);
        var myFragment: Fragment = RatingFragment()
        myFragment.setArguments(arguments);
        Log.d("args",arguments.toString())
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, myFragment)
                .addToBackStack(null)
                .commit()
        Log.d("popup","pop")



//
//        btn_back_home.setOnClickListener({
//            onBackPressed()
//        })

//        btnAddFav.setOnClickListener({
////            if (firebaseAuth.currentUser == null) {
////                Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show()
////            }
////            else {
////                //user is logged in, we can do favorite functionality
////                if (isInMyFavorite){
////                    // already in fav, remove
////                    removeFromFavorite()
////                }
////                else{
////                    //not in fav, add
////                    addToFavorite()
////                }
////            }
////        })
//    }

//    private fun checkIsFavorite(){
//        Log.d(TAG, "checkIsFavorite: Checking if recipe is in fav or not")
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(firebaseAuth.uid!!).child("Favorites").child(recipeId)
//            .addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    isInMyFavorite = snapshot.exists()
//                    if (isInMyFavorite){
//                        //available in favorite
//                        Log.d(TAG, "onDataChange: available in favorite")
//                            //set drawable top icon
//                        btnAddFav.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_favorite,0,0)
//                        btnAddFav.text = "Remove Favorite"
//                    }
//                    else{
//                        //not available in favorite
//                        Log.d(TAG, "onDataChange: not available in favorite")
//                        //set drawable top icon
//                        btnAddFav.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_unsavedwatch,0,0)
//                        btnAddFav.text = "Add Favorite"
//
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//    }
//
//    private fun addToFavorite(){
//        Log.d(TAG, "addToFavorite: Adding to fav")
//        val timestamp = System.currentTimeMillis()
//
//        //setup data to add in db
//        val hashMap = HashMap<String, Any>()
//        hashMap["recipeId"] = recipeId
//        hashMap["timestamp"] = timestamp
//
//        //save to db
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(firebaseAuth.uid!!).child("Favorites").child(recipeId)
//            .setValue(hashMap)
//            .addOnSuccessListener {
//                //added to fav
//                Log.d(TAG, "addToFavorite: Added to fav")
//            }
//            .addOnFailureListener { e->
//                //failed to add to fav
//                Log.d(TAG, "addToFavorite: Failed to add to fav due to ${e.message}")
//                Toast.makeText(this,"Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun removeFromFavorite(){
//        Log.d(TAG, "removeFromFavorite: Removing from fav")
//
//        //database ref
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.child(firebaseAuth.uid!!).child("Favorites").child(recipeId)
//            .removeValue()
//            .addOnSuccessListener {
//                Log.d(TAG, "removeFromFavorite: Removed from fav")
//                Toast.makeText(this,"Removed from fav", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e->
//                Log.d(TAG, "removeFromFavorite: Failed to remove from fave due to ${e.message}")
//                Toast.makeText(this,"Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()
//            }
    }
}
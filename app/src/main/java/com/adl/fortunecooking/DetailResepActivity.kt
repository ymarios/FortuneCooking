package com.adl.fortunecooking

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adl.fortunecooking.fragments.RatingFragment

import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_resep.*
import java.math.RoundingMode
import java.text.DecimalFormat

class DetailResepActivity : AppCompatActivity() {

    lateinit var data: ResepModel

    private var videosId = ""

//    //will hold a boolean value false/true to indicate either is in current user's favorite list or not
    private var isInMyFavorite = false

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setFullscreen()
        val currentUser = Firebase.auth.currentUser

        firebaseAuth = FirebaseAuth.getInstance()


        if (intent.hasExtra("data")) {
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

        if (firebaseAuth.currentUser != null){
            checkIsFavorite(data.id)
        }

        btnPlayVideo.setOnClickListener({
            val intent = Intent(this@DetailResepActivity, PlayVideo::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        })

        btn_set_rating.setOnClickListener({
            var arguments = Bundle()
            arguments.putParcelable("data", data);
            var myFragment: Fragment = RatingFragment()
            myFragment.setArguments(arguments);
            Log.d("args", arguments.toString())
            showDialog()

        })

//        isFavorite(data.id,currentUser!!)
//        btn_add_favorite.setOnClickListener({
//            btn_add_favorite.setCompoundDrawablesWithIntrinsicBounds(com.adl.fortunecooking.R.drawable.ic_savedwatch, 0, 0, 0);
//            addFavorite(data.id,currentUser!!)
//        })

        btn_add_favorite.setOnClickListener({
            if (isInMyFavorite){
                // already in fav, remove
                removeFromFavorite(data.id)
            }
            else{
                //not in fav, add
                addToFavorite(data.id)
            }
        })

        btn_back_home.setOnClickListener({
            onBackPressed()
        })
    }

    private fun checkIsFavorite(videosId: String){
        Log.d(TAG, "checkIsFavorite: Checking if recipe is in fav or not")

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavorite = snapshot.exists()
                    Log.d("Status", "onDataChange: ${snapshot.exists()}")
                    if (isInMyFavorite){
                        //available in favorite
                        Log.d(TAG, "onDataChange: available in favorite")
                        //set drawable top icon
                        btn_add_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_savedwatch,0,0,0)
                        btn_add_favorite.setText("My Favorite")
                    }
                    else{
                        //not available in favorite
                        Log.d(TAG, "onDataChange: not available in favorite")
                        //set drawable top icon
                        btn_add_favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unsavedwatch,0,0,0)
                        btn_add_favorite.setText("Add Favorite")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun addToFavorite(videosId:String){
        Log.d(TAG, "addToFavorite: Adding to fav")
        val timestamp = System.currentTimeMillis()

        //setup data to add in db
        val hashMap = HashMap<String, Any>()
        hashMap["videosId"] = videosId
        hashMap["timestamp"] = timestamp

        //save to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .setValue(hashMap)
            .addOnSuccessListener {
                //added to fav
                Log.d(TAG, "addToFavorite: Added to fav")
            }
            .addOnFailureListener { e->
                //failed to add to fav
                Log.d(TAG, "addToFavorite: Failed to add to fav due to ${e.message}")
                Toast.makeText(this,"Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFavorite(videosId:String){
        Log.d(TAG, "removeFromFavorite: Removing from fav")

        //database ref
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!).child("Favorites").child(videosId)
            .removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "removeFromFavorite: Removed from fav")
                Toast.makeText(this,"Removed from fav", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Log.d(TAG, "removeFromFavorite: Failed to remove from fave due to ${e.message}")
                Toast.makeText(this,"Failed to remove from fav due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun showDialog() {
        val fragmentManager = supportFragmentManager

        var arguments = Bundle()
        arguments.putParcelable("data", data);
        var myFragment: Fragment = RatingFragment()
        myFragment.setArguments(arguments);
        Log.d("args", arguments.toString())
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
        Log.d("popup", "pop")
    }
//    fun isFavorite(vidId:String,currentUser:FirebaseUser){
////        val currentUser = Firebase.auth.currentUser
//        var found = false
//        val rootRef = FirebaseDatabase.getInstance().reference
//        val usersRef = rootRef.child("Rates")
//        val okQuery = usersRef.orderByChild("videoId").equalTo("${vidId}")
//        val valueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (ds in dataSnapshot.children) {
//                    if(ds.exists()){
//                        val userId = ds.child("userId").getValue(String::class.java)!!
//                        Log.d("status","${userId}")
//                        if(userId == currentUser?.uid){
//                            found = true
//                        }else{
//                            found = false
//                        }
//                    }
//                }
//                Log.d("status","${found}")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d("TAG", databaseError.getMessage()) //Don't ignore errors!
//            }
//        }
//        okQuery.addListenerForSingleValueEvent(valueEventListener)
//        Log.d("status2","${found}")
//    }

    //    fun addFavorite(vidId:String,currentUser:FirebaseUser){
//        val timestamp = ""+System.currentTimeMillis()
//
//        val hashMap = HashMap<String, Any>()
//        hashMap["id"] = "$timestamp"
//        hashMap["videoId"] = "$vidId"
//        hashMap["userId"] = currentUser.uid
//        hashMap["status"] = "true"
//
//        val dbReference = FirebaseDatabase.getInstance().getReference("Favorites")
//        dbReference.child(timestamp)
//            .setValue(hashMap)
//            .addOnSuccessListener { taskSnapshot ->
//                Log.d("addrate","Succeses")
//            }
//            .addOnFailureListener{ e ->
//                Log.d("addrate","failed")
//            }
//
//    }
    fun setFullscreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}

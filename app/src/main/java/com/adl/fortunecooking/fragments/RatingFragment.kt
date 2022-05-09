package com.adl.fortunecooking.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.adl.fortunecooking.DashboardActivity
import com.adl.fortunecooking.R
import com.adl.fortunecooking.model.Rating
import com.adl.fortunecooking.model.ResepModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_rating.*
import java.math.RoundingMode
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RatingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RatingFragment : DialogFragment(){
    lateinit var database: DatabaseReference
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RatingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RatingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = Firebase.auth.currentUser
        btn_submit_rate.setOnClickListener({
            val rateVal = rate_video.getRating()
            val bundle = arguments
            val model: ResepModel? =bundle?.getParcelable("data")
            Log.d("rateIS =","resep = ${model?.title} + user = ${model?.uid} + rate = ${rateVal}")
            if(model != null && user != null){
               addVideoRate(model.id,user.uid,rateVal)
                dismiss()
                activity.let{
                    activity?.finish()
                }
            }

        })


    }

    fun addVideoRate(vidId:String,userId:String,rating:Float){
        val timestamp = ""+System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["videoId"] = "$vidId"
        hashMap["userId"] = "$userId"
        hashMap["ratingValue"] = "$rating"



        val dbReference = FirebaseDatabase.getInstance().getReference("Rates")
            dbReference.child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener { taskSnapshot ->
              Log.d("addrate","Succeses")
            }
            .addOnFailureListener{ e ->
                Log.d("addrate","failed")
            }
        calcurate(vidId)
    }

    fun calcurate(vidId:String){
        var temp: Float=0.0f
        var count= 0
        val rootRef = FirebaseDatabase.getInstance().reference
        val usersRef = rootRef.child("Rates")
        val okQuery = usersRef.orderByChild("videoId").equalTo("${vidId}")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if(ds.exists()){
                        val rateVal = ds.child("ratingValue").getValue(String::class.java)!!.toFloat()
                        temp += rateVal
                        count += 1
                    }

                    Log.d("isi","${ds.child("ratingValue").getValue(String::class.java)}")
                }
                Log.d("isi","${temp} + ${count}")
                val mean = temp / count
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(mean)
                updateRate(roundoff.toFloat(),vidId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        okQuery.addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateRate(res:Float,vidId:String){
        val ref = FirebaseDatabase.getInstance().getReference("Videos").child(vidId)
        println("!!!!!!!!!!! Firebase reference: ${ref.toString()}")


        ref.child("rating").setValue(res.toString())

    }


}
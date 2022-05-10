package com.adl.fortunecooking.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.fortunecooking.R
import com.adl.fortunecooking.adapter.ResepAdapter
import com.adl.fortunecooking.adapter.SearchAdapter
import com.adl.fortunecooking.model.ResepModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_home.*

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [FavoritesFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
class FavoritesFragment : Fragment() {
    lateinit var lstDataResep : ArrayList<ResepModel>
    lateinit var database: DatabaseReference
    lateinit var favAdapter: SearchAdapter
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentUser = Firebase.auth.currentUser
        database= FirebaseDatabase.getInstance().reference.child("Users").child("${currentUser?.uid}").child("Favorites")
        LoadDataFirebase()


//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment FavoritesFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FavoritesFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }

    fun getFavorite(vidId:String){
        database= FirebaseDatabase.getInstance().reference.child("Videos")
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
//                lstDataResep.clear()
                for (data in snapshot.children){
                    if(data.child("id").getValue(String::class.java) == vidId){
                        val namaresep= data.child("title").getValue(String::class.java)
                        val imagelink = data.child("ImageUri").getValue(String::class.java)
                        val videolink = data.child("videoUri").getValue(String::class.java)
                        val userUid = data.child("userId").getValue(String::class.java)
                        val rating = data.child("rating").getValue(String::class.java)
                        val idVideo = data.child("id").getValue(String::class.java)
                        val desc = data.child("Deskripsi").getValue(String::class.java)
                        val resep = data.child("Resep").getValue(String::class.java)
                        val step = data.child("Step").getValue(String::class.java)
                        lstDataResep.add( ResepModel(idVideo.toString(),namaresep.toString(),userUid.toString(),imagelink.toString(), videolink.toString(),rating.toString(),resep.toString(),step.toString(), desc.toString()))
                    }

                    // Log.d("TAG", "nama: ${namaresep}\nimagelink: ${imagelink}")
                }
                favAdapter.notifyDataSetChanged()

            } else {
                Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
            }
        }
        lstDataResep = ArrayList<ResepModel>()

        favAdapter = SearchAdapter(lstDataResep)
        rcFavorite.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = favAdapter
        }
//        rcFavorite.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,true)
//            adapter = resepAdapter
//        }
    }

    fun LoadDataFirebase(){

        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
//                    lstDataResep.clear()
                for (data in snapshot.children){
                    val videosId = data.child("videosId").getValue(String::class.java)
                    val timestamp = data.child("timestamp").getValue(String::class.java)
//                        lstDataResep.add( ResepModel(idVideo.toString(),namaresep.toString(),userUid.toString(),imagelink.toString(), videolink.toString(),rating.toString(),resep.toString(),step.toString(), desc.toString()))
                     Log.d("favorite", "${videosId}")
                    getFavorite(videosId!!)
                }
//                    resepAdapter.notifyDataSetChanged()

            } else {
                Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
            }
        }


    }

}
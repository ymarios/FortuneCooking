package com.adl.fortunecooking.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.fortunecooking.R
import com.adl.fortunecooking.adapter.ResepAdapter
import com.adl.fortunecooking.adapter.SearchAdapter
import com.adl.fortunecooking.model.ResepModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    lateinit var database: DatabaseReference
    lateinit var resepAdapter: ResepAdapter
    lateinit var searchAdapter:SearchAdapter
    lateinit var lstDataResep : ArrayList<ResepModel>
    lateinit var lstSearch:ArrayList<ResepModel>
    lateinit var searchString:String

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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lstDataResep = ArrayList<ResepModel>()
        lstSearch = ArrayList<ResepModel>()
        //resepAdapter = ResepAdapter(lstDataResep)
        searchAdapter = SearchAdapter(lstSearch)


        database= FirebaseDatabase.getInstance().reference.child("Videos")

        btnSearch.setOnClickListener{
            searchString = txtSearch.text.toString()
            database.get().addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    lstDataResep.clear()
                    for (data in snapshot.children){
                        val namaresep= data.child("title").getValue(String::class.java)
                        val imagelink = data.child("ImageUri").getValue(String::class.java)
                        val videolink = data.child("videoUri").getValue(String::class.java)
                        val uid = data.child("userId").getValue(String::class.java)
                        val rating = data.child("rating").getValue(String::class.java)
                        val idVideo = data.child("id").getValue(String::class.java)
                        val desc = data.child("Deskripsi").getValue(String::class.java)
                        val resep = data.child("Resep").getValue(String::class.java)
                        val step = data.child("Step").getValue(String::class.java)

                        lstDataResep.add( ResepModel(idVideo.toString(),namaresep.toString(),uid.toString(),imagelink.toString(), videolink.toString(),rating.toString(),resep.toString(),step.toString(), desc.toString()))
                        // Log.d("TAG", "nama: ${namaresep}\nimagelink: ${imagelink}")
                    }
                    // resepAdapter.notifyDataSetChanged()
                    Search(lstDataResep)
                } else {
                    Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
                }
            }
        }


    }

    fun Search(list:ArrayList<ResepModel>){
        val pattern = searchString.toRegex((RegexOption.IGNORE_CASE))
        lstSearch.clear()
        for(dataresep in list){
            if (pattern.containsMatchIn(dataresep.title)) {
                println("${dataresep.title} matches")
                val resep:String=dataresep.title.toString()
                //Toast.makeText(activity,"${dataresep.title} matches",Toast.LENGTH_LONG).show()
                lstSearch.add( ResepModel(dataresep.id,dataresep.title,dataresep.uid,dataresep.ImageUri,dataresep.videoUri,dataresep.rating,dataresep.Resep,dataresep.Step, dataresep.Deskripsi))
            }
        }
        searchAdapter.notifyDataSetChanged()

        rcycSearch.apply {
            layoutManager=LinearLayoutManager(activity)
            adapter=searchAdapter
        }
    }




}



package com.adl.fortunecooking.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adl.fortunecooking.AddVideoActivity
import com.adl.fortunecooking.DetailResepActivity
import com.adl.fortunecooking.R
import com.adl.fortunecooking.adapter.ResepAdapter
import com.adl.fortunecooking.model.ResepModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    lateinit var database: DatabaseReference

    lateinit var lstDataResep : ArrayList<ResepModel>
    lateinit var  resepAdapter: ResepAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        addVideoFab.setOnClickListener {
//            startActivity(Intent(this,AddVideoActivity::class.java))
//        }
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        database= FirebaseDatabase.getInstance().reference.child("Videos")

        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                lstDataResep.clear()
                for (data in snapshot.children){
                    val namaresep = data.child("title").getValue(String::class.java)
                    val imagelink = data.child("ImageUri").getValue(String::class.java)
                    lstDataResep.add( ResepModel(namaresep.toString() ,imagelink.toString()))
                    resepAdapter.notifyDataSetChanged()

                    Log.d("TAG", "nama: ${namaresep}\nimagelink: ${imagelink}")
                }


            } else {
                Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
            }
        }
        lstDataResep = ArrayList<ResepModel>()

        resepAdapter = ResepAdapter(lstDataResep)

        rvFood.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = resepAdapter
        }
        addVideoFab.setOnClickListener({
            activity?.let{
                val intent = Intent(it, AddVideoActivity::class.java)
                it.startActivity(intent)
            }
        })
    }
}
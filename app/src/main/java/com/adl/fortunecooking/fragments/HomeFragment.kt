package com.adl.fortunecooking.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adl.fortunecooking.AddVideoActivity
import com.adl.fortunecooking.DetailResepActivity
import com.adl.fortunecooking.R
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

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
        addVideoFab.setOnClickListener({
            activity?.let{
                val intent = Intent(it, AddVideoActivity::class.java)
                it.startActivity(intent)
            }
        })
    }
}
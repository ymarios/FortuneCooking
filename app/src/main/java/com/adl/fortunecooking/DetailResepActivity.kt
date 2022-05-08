package com.adl.fortunecooking

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adl.fortunecooking.fragments.RatingFragment

import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_detail_resep.*
import kotlinx.android.synthetic.main.fragment_rating.*

class DetailResepActivity : AppCompatActivity() {
    lateinit var data:ResepModel

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

        }
        btnPlayVideo.setOnClickListener({
            val intent = Intent(this@DetailResepActivity, PlayVideo::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        })

        btn_set_rating.setOnClickListener({
            showDialog()
        })


    }
    fun showDialog() {
        val fragmentManager = supportFragmentManager
        val newFragment = RatingFragment()
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        Log.d("popup","pop")
    }

    override fun onCreateDialog(id: Int): Dialog {
        return super.onCreateDialog(id)
        val builder = AlertDialog.Builder(this)
        builder.create()

    }


}
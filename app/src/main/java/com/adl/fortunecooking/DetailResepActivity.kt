package com.adl.fortunecooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_detail_resep.*

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

        }
        btnPlayVideo.setOnClickListener({
            val intent = Intent(this@DetailResepActivity, PlayVideo::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        })
    }
}
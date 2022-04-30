package com.adl.fortunecooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_resep.*

class DetailResepActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)

        btnPlayVideo.setOnClickListener({
            val intent = Intent(this@DetailResepActivity, PlayVideo::class.java)
            startActivity(intent)
        })
    }
}
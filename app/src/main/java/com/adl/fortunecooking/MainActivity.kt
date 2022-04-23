package com.adl.fortunecooking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

//        val windowInsetsController =
//            ViewCompat.getWindowInsetsController(window.decorView) ?: return
//        // Configure the behavior of the hidden system bars
//        windowInsetsController.systemBarsBehavior =
//            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        // Hide both the status bar and the navigation bar
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())


        Handler().postDelayed({ //This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(i)
            // close this activity
            finish()
        }, 10000)

//        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
//        finish()
    }
}

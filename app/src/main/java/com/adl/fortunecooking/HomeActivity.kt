package com.adl.fortunecooking

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.adl.fortunecooking.fragments.AccountFragment
import com.adl.fortunecooking.fragments.FavoritesFragment
import com.adl.fortunecooking.fragments.HomeFragment
import com.adl.fortunecooking.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_holder.*

class HomeActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val favoritesFragment = FavoritesFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replacementFragment(homeFragment)
        setFullscreen()

        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.ic_home -> replacementFragment(homeFragment)
                R.id.ic_search -> replacementFragment(searchFragment)
                R.id.ic_favorite -> replacementFragment(favoritesFragment)
                R.id.ic_account -> replacementFragment(accountFragment)
            }
            true
        }
    }

    private fun replacementFragment(fragment: Fragment){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }




    fun setFullscreen(){
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
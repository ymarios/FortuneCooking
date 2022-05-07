package com.adl.fortunecooking

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adl.fortunecooking.fragments.AccountFragment
import com.adl.fortunecooking.fragments.FavoritesFragment
import com.adl.fortunecooking.fragments.HomeFragment
import com.adl.fortunecooking.fragments.SearchFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class DashboardActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val favoritesFragment = FavoritesFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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
//            transaction.setCustomAnimations(R.anim.sllide_in_right,R.anim.slide_in_left)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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
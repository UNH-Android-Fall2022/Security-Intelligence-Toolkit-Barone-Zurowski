package com.toolkit.sit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.fragments.*
import com.toolkit.sit.fragments.authenticated.*
import com.toolkit.sit.util.Util


class SITActivity : AppCompatActivity() {

    private var TAG = "_SitActivity"
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar
//    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SecurityIntelligenceToolkit)
        super.onCreate(savedInstanceState)
        var email = FirebaseAuth.getInstance().currentUser?.email

        Log.i(TAG, "User is logged in: $email")

        setContentView(R.layout.app_layout)

        topNavigationView = findViewById(R.id.topAppBar)
        bottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnItemSelectedListener { menuItem : MenuItem ->
            bottomNavIconClicked(menuItem)
            true
        }

        topNavigationView.setOnMenuItemClickListener { menuItem: MenuItem ->

            if(menuItem.itemId == R.id.SettingsFragment) {
                Log.d(TAG, "Settings")
                setFrag(SettingsFragment())
            }
            true
        }
    }

    // handle bottom nav bar icon clicked
    private fun bottomNavIconClicked(menuItem: MenuItem) {
        when(menuItem.itemId) {
            R.id.HomeFragment -> {
                Log.d(TAG, "Home")
                setFrag(HomeFragment())
            }
            R.id.ScanFragment -> {
                Log.d(TAG, "Scan")
                setFrag(ScanFragment())
            }
            R.id.ShodanFragment -> {
                Log.d(TAG, "Shodan")
                setFrag(ShodanFragment())
            }
            R.id.HistoryFragment -> {
                Log.d(TAG, "History")
                setFrag(HistoryFragment(supportFragmentManager))
            }
        }
    }

    // Function used to set fragment container to specific fragment.
    private fun setFrag(fragment: Fragment) {
        Util.setFragment(supportFragmentManager, R.id.fragment_container_view,fragment)
    }

}
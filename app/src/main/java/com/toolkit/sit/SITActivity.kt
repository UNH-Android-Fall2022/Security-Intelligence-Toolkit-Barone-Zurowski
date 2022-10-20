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


class SITActivity : AppCompatActivity() {

    private var TAG = "_SitActivity"
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar
//    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SecurityIntelligenceToolkit)
        super.onCreate(savedInstanceState)
        var user = FirebaseAuth.getInstance().getCurrentUser()

        Log.i(TAG, "User is logged in: ${user}")

        setContentView(R.layout.app_layout)


        topNavigationView = findViewById(R.id.topAppBar)
        bottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnItemSelectedListener { menuItem : MenuItem ->
            when(menuItem.itemId) {
                R.id.HomeFragment -> {
                    Log.d(TAG, "Home")
                    setFragment(HomeFragment())
                }
                R.id.ScanFragment -> {
                    Log.d(TAG, "Scan")
                    setFragment(ScanFragment())

                }
                R.id.ShodanFragment -> {
                    Log.d(TAG, "Shodan")
                    setFragment(ShodanFragment())
                }
                R.id.HistoryFragment -> {
                    Log.d(TAG, "History")
                    setFragment(HistoryFragment())
                }
            }
            true
        }

        topNavigationView.setOnMenuItemClickListener { menuItem: MenuItem ->

            if(menuItem.itemId == R.id.SettingsFragment) {
                Log.d(TAG, "Settings")
                setFragment(SettingsFragment())
            }
            true
        }



    }

    private fun setFragment(fragment: Fragment) {
        // Create new fragment and transaction

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment).commit()

    }
}
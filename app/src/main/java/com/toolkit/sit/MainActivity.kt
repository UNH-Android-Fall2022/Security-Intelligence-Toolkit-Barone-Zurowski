package com.toolkit.sit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private var TAG = "_MainActivity"
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // will want to do it first for login layout but for testing purposes.
        setContentView(R.layout.app_layout)


        topNavigationView = findViewById(R.id.topAppBar)
        bottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnItemReselectedListener {menuItem : MenuItem ->
            when(menuItem.itemId) {
                R.id.HomeFragment -> {
                    Log.d(TAG, "Home")
                }
                R.id.ScanFragment -> {
                    Log.d(TAG, "Scan")
                }
                R.id.ShodanFragment -> {
                    Log.d(TAG, "Shodan")
                }
                R.id.HistoryFragment -> {
                    Log.d(TAG, "History")
                }

            }
        }

        topNavigationView.setOnMenuItemClickListener { menuItem: MenuItem ->

            if(menuItem.itemId == R.id.SettingsFragment) {
                Log.d(TAG, "Settings")
            }
            true
        }
    }
}
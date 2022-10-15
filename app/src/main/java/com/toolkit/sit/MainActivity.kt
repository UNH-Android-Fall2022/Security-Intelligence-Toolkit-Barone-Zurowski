package com.toolkit.sit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.toolkit.sit.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var TAG = "SIT_TAG_MAIN"
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                true
            }
            false
        }
    }
}
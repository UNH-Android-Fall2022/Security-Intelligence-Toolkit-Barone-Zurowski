package com.toolkit.sit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import kotlin.math.log


class SITActivity : AppCompatActivity() {

    private var TAG = "_SitActivity"
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar

    private val CHANNEL_ID = "com.toolkit.sit.channel_1"

//    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SecurityIntelligenceToolkit)
        super.onCreate(savedInstanceState)
        var email = FirebaseAuth.getInstance().currentUser?.email

        Log.i(TAG, "User is logged in: $email")

        setContentView(R.layout.app_layout)

        createNotificationChannel()

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
            Log.d(TAG, menuItem.itemId.toString())
            true
        }
    }

    // handle bottom nav bar icon clicked
    private fun bottomNavIconClicked(menuItem: MenuItem) {
        Log.d(TAG, menuItem.toString())
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SIT Notifications"
            val descriptionText = "SIT Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
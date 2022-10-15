package com.toolkit.sit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private var TAG = "_MainActivity"
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var topNavigationView : MaterialToolbar
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val collection = database.collection("test")

        collection.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

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
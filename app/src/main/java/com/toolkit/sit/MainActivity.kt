package com.toolkit.sit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    private lateinit var button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            changeView()
        }
    }

    private fun changeView() {
        val intent = Intent(this, SITActivity::class.java)
        startActivity(intent)
    }

    fun setFragment(fragment: Fragment) {
        // Create new fragment and transaction
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment).commit()
    }
}
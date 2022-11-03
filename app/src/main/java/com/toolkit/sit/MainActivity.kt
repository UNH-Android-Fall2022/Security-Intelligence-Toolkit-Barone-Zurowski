package com.toolkit.sit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAuth = FirebaseAuth.getInstance().currentUser
        // if the user is authenticated already change view to logged in.
        if(isAuth != null) {
            changeView()
            return
        }
        setContentView(R.layout.activity_main)

    }

    // view if the user is authenticated
    private fun changeView() {
        val intent = Intent(this, SITActivity::class.java)
        startActivity(intent)
    }

    // function to change the fragment.
    fun setFragment(fragment: Fragment) {
        // Create new fragment and transaction
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment).commit()
    }
}
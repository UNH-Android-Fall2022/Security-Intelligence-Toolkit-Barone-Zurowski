package com.toolkit.sit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.util.Util.hideSoftKeyboard


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
        setupKbdListener(findViewById(R.id.main_parent))

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

    private fun setupKbdListener(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                this.hideSoftKeyboard()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupKbdListener(innerView)
            }
        }
    }
}
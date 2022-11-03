package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.MainActivity
import com.toolkit.sit.R
import com.toolkit.sit.SITActivity

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private var TAG = "SETTINGS_FRAGMENT"
    private lateinit var logoutButton: Button
    private lateinit var appContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        logoutButton = view.findViewById(R.id.button_log_out)
        appContext = view.context

        return view
    }

    override fun onStart() {
        super.onStart()

        // used to allow the user to log out in the settings.
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(appContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
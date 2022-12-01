package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.toolkit.sit.MainActivity
import com.toolkit.sit.R
import com.toolkit.sit.util.Util


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private var TAG = "SETTINGS_FRAGMENT"
    private lateinit var logoutButton: Button
    private lateinit var apiKeyButton: Button
    private lateinit var apiKeyEditText: EditText
    private lateinit var appContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        apiKeyButton = view.findViewById(R.id.change_api_key)
        apiKeyEditText = view.findViewById(R.id.apiKeySet)
        logoutButton = view.findViewById(R.id.button_log_out)
        appContext = view.context

        return view
    }

    override fun onStart() {
        super.onStart()

        apiKeyButton.setOnClickListener {
            val shodanAPIKey = apiKeyEditText.text.toString()
            if(!Util.checkFieldsIfEmpty(shodanAPIKey)) {
                val uuid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val userMap: Map<String, String> = mapOf("shodanKey" to shodanAPIKey)
                FirebaseFirestore.getInstance()
                    .collection("settings")
                    .document(uuid).set(userMap)

            }

        }
        // used to allow the user to log out in the settings.
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(appContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
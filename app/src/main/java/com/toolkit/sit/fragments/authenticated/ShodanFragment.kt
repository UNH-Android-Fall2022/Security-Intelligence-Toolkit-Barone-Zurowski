package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.toolkit.sit.R
import com.toolkit.sit.shodan.ShodanAPI
import com.toolkit.sit.shodan.ShodanRetrofitClient
import com.toolkit.sit.util.Util
import com.toolkit.sit.util.Util.setShodanKey
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ShodanFragment.newInstance] factory method to
 * create an instance of this fragment. This will be modified later on
 */
class ShodanFragment : Fragment() {

    private lateinit var shodanSpinner: Spinner
    private lateinit var shodanButton: Button
    private lateinit var appContext: Context
    private lateinit var shodanOptions: Array<String>
    private lateinit var queryShodan: EditText

    private var TAG = "SHODAN_FRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reference = FirebaseFirestore.getInstance()
            .collection("settings")
            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid.toString())

        reference.get().addOnSuccessListener { document ->
            if (document != null) {
                var data = document.documents.first().data as Map<*, *>;
                Log.d(TAG, "OBJ: ${data.get("shodanKey")}")

                setShodanKey(data.get("shodanKey").toString())
            }
        }

        val view: View = inflater.inflate(R.layout.fragment_shodan, container, false)

        shodanButton = view.findViewById(R.id.shodanButton)
        shodanSpinner = view.findViewById(R.id.shodan_spinner)
        queryShodan = view.findViewById(R.id.queryShodan)

        appContext = view.context.applicationContext
        shodanOptions = appContext.resources.getStringArray(R.array.shodan_array)
        return view
    }

    override fun onStart() {
        super.onStart()
        val adapter = ArrayAdapter(appContext,
            R.layout.spinner_layout, shodanOptions)
        shodanSpinner.adapter = adapter

        shodanButton.setOnClickListener {
            Log.d(TAG, "Clicked button.")
            val selected = shodanSpinner.selectedItem.toString()

            when(selected) {
                "My Public IP" -> {
                    getPublicIP()
                }
                "Search IP" -> {
                    val ip = queryShodan.text.toString()
                    if(!Util.checkFieldsIfEmpty(ip)) {
                        Log.d(TAG, "IP Searching: $ip")
                        searchIP(ip)
                    }
                }


            }
        }

    }
    private fun searchIP(ip: String) {
        GlobalScope.launch {
            val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()

            val data = shodanAPI.searchIP(ip)

            Log.d(TAG, "My Public Address: $data")
        }
    }
    private fun getPublicIP() {
        GlobalScope.launch {
            val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()

            val ip = shodanAPI.getPublicIPv4Addr()

            Log.d(TAG, "My Public Address: $ip")
        }
    }

}
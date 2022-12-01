package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toolkit.sit.R
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.shodan.ShodanAPI
import com.toolkit.sit.shodan.ShodanRetrofitClient
import com.toolkit.sit.util.Util
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

        Util.setShodanKey("hFEldaF51xbka9OUYUEkvuKXLdUuKEs0")

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
                    } else {
                        Util.popUp(appContext, "Please Enter a Value to search IP", Toast.LENGTH_LONG)
                    }
                }
                "Search Filter (e.g. webcam)" -> {
                    val query = queryShodan.text.toString()
                    if(!Util.checkFieldsIfEmpty(query)) {
                        Log.d(TAG, "Search filter: $query")
                        searchFilter(query)
                    } else {
                        Util.popUp(appContext, "Please Enter a Value to search Filter", Toast.LENGTH_LONG)
                    }
                }
            }
        }

    }

    private fun searchFilter(filter: String) {
        GlobalScope.launch {
            val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()
            val data = shodanAPI.search(filter)

            if(data.matches.isNotEmpty()) {
                val match = data.matches.last()
                var data = "Title: ${match.title}"

                if(match.query?.isNotEmpty() == true) {
                    data += "\nQuery Found: ${match.query}"
                }
                if (match.description?.isNotEmpty() == true) {
                    data += "\nDescription: ${match.description}"
                }


                val model = NetworkScanModel(
                    createdTime = Timestamp.now(),
                    uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    scanType = "SHODAN_FILTER_SEARCH",
                    results = mutableListOf(
                        hashMapOf(data to listOf())
                    ),
                    attemptedScan = filter,
                    isLocalScan = false,
                    isNetworkScan = false
                )
                //
                val db = Firebase.firestore
                db.collection("scans").add(model)
            }



            Log.d(TAG, "Shodan Search: $data")
        }
    }
    private fun searchIP(ip: String) {
        GlobalScope.launch {
            val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()

            val data = shodanAPI.searchIP(ip)

            val openPortsMap: MutableMap<String, List<Int>> = HashMap()

            openPortsMap[ip] = data.ports as MutableList<Int>
            val info = "${data.city}, ${data.regionCode}, ${data.countryName}, ${data.org}"
            val model = NetworkScanModel(
                createdTime = Timestamp.now(),
                uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                scanType = "SHODAN_SEARCH_IP",
                results = mutableListOf(
                    openPortsMap
                ),
                attemptedScan = info,
                isLocalScan = false,
                isNetworkScan = false
            )
            //
            val db = Firebase.firestore
            db.collection("scans").add(model)

            Log.d(TAG, "Search Address: $model")
        }
    }
    private fun getPublicIP(): String {

        var ip = "ERROR"
        GlobalScope.launch {
            val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()

            ip = shodanAPI.getPublicIPv4Addr()

            val db = Firebase.firestore

            Log.d(TAG, "My Public Address: $ip")

            val model = NetworkScanModel(
                createdTime = Timestamp.now(),
                uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                scanType = "SHODAN_PUBLIC_IP",
                results = mutableListOf(
                        hashMapOf("SHODAN_PUBLIC_SCAN" to listOf())
                ),
                attemptedScan = ip,
                isLocalScan = false,
                isNetworkScan = false
            )
            // add to the database using the network Model.
            Log.d(TAG, model.toString())
            db.collection("scans").add(model)
        }

        return ip
    }

}
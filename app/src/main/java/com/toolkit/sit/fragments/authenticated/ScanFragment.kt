package com.toolkit.sit.fragments.authenticated

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toolkit.sit.R
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.scanner.NetScanner
import com.toolkit.sit.util.Util
import com.toolkit.sit.util.Util.isCIDR
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment() {

    private lateinit var remoteScanButton: Button
    private lateinit var buttonStartLocalScan: Button
    private lateinit var editTextScanField: EditText
    private lateinit var appContext: Context
    private var TAG = "SCAN_FRAGMENT"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_scan, container, false)
        remoteScanButton = view.findViewById(R.id.buttonStartRemoteScan)
        editTextScanField = view.findViewById(R.id.editTextRemoteSubnet)
        buttonStartLocalScan = view.findViewById(R.id.buttonStartLocalScan)
        appContext = view.context.applicationContext
        return view
    }

    override fun onStart() {
        super.onStart()
        val scanner = NetScanner()
        remoteScanButton.setOnClickListener {
            val subnet = editTextScanField.text.toString()
            // validation if data is entered
            if (!Util.checkFieldsIfEmpty(subnet) && subnet.isCIDR()) {
                // 150 timeout is generally what non local subnet hosts are required to scan
                runScanAndWrite(scanner, subnet, false, 150)
            } else {
                Util.popUp(appContext,"Please Enter Valid CIDR address", Toast.LENGTH_SHORT)
            }
        }

        // get the local CIDR notation so the user knows what the local scan will be
        buttonStartLocalScan.text = "Start Local Scan (${getLocalCIDR()})"


        // automatically do local scan if clicked
        buttonStartLocalScan.setOnClickListener {
            val localCIDR = getLocalCIDR()
            runScanAndWrite(scanner, localCIDR, true, 20)
        }
    }

    private fun runScanAndWrite(scanner: NetScanner, subnet: String, isLocalScan: Boolean, timeout: Int) {
        // create coroutine otherwise will block app
        GlobalScope.launch(Dispatchers.IO) {
            val openAddresses = scanner.remoteScan(subnet, timeout) // this is where scan is done
            Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())

            val db = Firebase.firestore
            // add to the database using the network Model.
            db.collection("scans").add(
                NetworkScanModel(
                    createdTime = Timestamp.now(),
                    results = openAddresses,
                    isLocalScan = isLocalScan,
                    attemptedScan = subnet,
                    uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
            )

            Log.d(TAG, "Open hosts $openAddresses")
        }
    }

    // this function gets the local CIDR
    private fun getLocalCIDR():String {
        val connectivityManager = appContext.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

        // used to check if the user is connected to wifi
        val capabilties = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return ""

        // assure that the scan is done via WIFI
        if(!capabilties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i(TAG, "Wifi is not enabled")
            return ""
        }
        val linkProp =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork)

        // check route information from the network to determine the local subnet
        val correctIface = linkProp!!.routes.filter {
            it.`interface` == "wlan0"
                && it.destination.toString().isCIDR()
                && it.destination.toString() != "0.0.0.0/0"
        }
        if (correctIface.isEmpty()) {
            return ""
        }
        return correctIface[0].destination.toString()
    }



}
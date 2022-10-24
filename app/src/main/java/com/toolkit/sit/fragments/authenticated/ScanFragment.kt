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
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toolkit.sit.R
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.scanner.NetScanner
import com.toolkit.sit.util.Util
import com.toolkit.sit.util.Util.isCIDR
import kotlinx.coroutines.*
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
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
            if (!Util.checkFieldsIfEmpty(subnet) && subnet.isCIDR()) {
                runScanAndWrite(scanner, subnet, false, 150)
            } else {
                Util.popUp(appContext,"Please Enter Valid CIDR address", Toast.LENGTH_SHORT)
            }
        }

        buttonStartLocalScan.setOnClickListener {
            val localCIDR = getLocalCIDR()
            runScanAndWrite(scanner, localCIDR, true, 20)
        }
    }

    private fun runScanAndWrite(scanner: NetScanner, subnet: String, isLocalScan: Boolean, timeout: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val openAddresses = scanner.remoteScan(subnet, timeout)
            Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())

            val db = Firebase.firestore
            db.collection("scans").add(
                NetworkScanModel(
                    CreatedTime = Timestamp.now(),
                    Results = openAddresses,
                    IsLocalScan = isLocalScan,
                    AttemptedScan = subnet,
                    uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
            )

            Log.d(TAG, "Open hosts $openAddresses")
        }
    }

    private fun getLocalCIDR():String {
        val connectivityManager = appContext.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilties = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return ""

        if(!capabilties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i(TAG, "Wifi is not enabled")
            return ""
        }
        val linkProp =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork)

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
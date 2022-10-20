package com.toolkit.sit.fragments.authenticated

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.R
import com.toolkit.sit.scanner.NetScanner
import com.toolkit.sit.util.Util
import com.toolkit.sit.util.Util.isCIDR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


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
        val view: View = inflater!!.inflate(R.layout.fragment_scan, container, false)
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
            Log.d(TAG, "Button Pressed")
            val subnet = editTextScanField.text.toString()
            Log.d(TAG, "${subnet.isCIDR()}")
            if (!Util.checkFieldsIfEmpty(subnet) && subnet.isCIDR()) {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val openAddresses = scanner.remoteScan(subnet, 150)
                        Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())
                        Log.d(TAG, "Open hosts $openAddresses")
                    }
                }
            } else {
                Util.popUp(appContext,"Please Enter Valid CIDR address", Toast.LENGTH_SHORT)
            }
        }

        buttonStartLocalScan.setOnClickListener {
            val localCIDR = getLocalCIDR()
            Log.i(TAG, "Local CIDR: $localCIDR")
            runBlocking {
                withContext(Dispatchers.IO) {
                    val openAddresses = scanner.remoteScan(localCIDR, 20)
                    Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())
                    Log.d(TAG, "Open hosts $openAddresses")
                }
            }
        }
    }

    private fun getLocalCIDR():String {
        val connectivityManager = appContext.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

        val linkProp =  connectivityManager.getLinkProperties(connectivityManager.activeNetwork)!!
        Log.i("myNetworkType: ", connectivityManager.activeNetwork.toString())

//        val localIP = linkProp.linkAddresses[1].toString().split("/")[0]

        return linkProp.routes[3].destination.toString()
    }



}
package com.toolkit.sit.fragments.authenticated

import android.app.NotificationManager
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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toolkit.sit.R
import com.toolkit.sit.SITActivity
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.scanner.NetScanner
import com.toolkit.sit.util.Util
import com.toolkit.sit.util.Util.hideSoftKeyboard
import com.toolkit.sit.util.Util.isCIDR
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment() {

    private lateinit var remoteScanButton: Button
    private lateinit var buttonStartLocalScan: Button
    private lateinit var editTextScanField: EditText
    private lateinit var textLocalScan: TextView
    private lateinit var appContext: Context
    private var TAG = "SCAN_FRAGMENT"

    private val CHANNEL_ID = "com.toolkit.sit.channel_1"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_scan, container, false)
        remoteScanButton = view.findViewById(R.id.buttonStartRemoteScan)
        editTextScanField = view.findViewById(R.id.editTextRemoteSubnet)
        buttonStartLocalScan = view.findViewById(R.id.buttonStartLocalScan)
        textLocalScan = view.findViewById(R.id.textLocalScan)
        appContext = view.context.applicationContext
        return view
    }

    override fun onStart() {
        super.onStart()
        val scanner = NetScanner()
        remoteScanButton.setOnClickListener {
            (activity as SITActivity).hideSoftKeyboard()

            val subnet = editTextScanField.text.toString()
            // validation if data is entered
            if (!Util.checkFieldsIfEmpty(subnet) && subnet.isCIDR()) {

                // Prevent running out of memory for too many addresses.
                val cidrNumber = subnet.split("/")[1].toIntOrNull()

                if (cidrNumber == null || cidrNumber < 16) {
                    Util.popUp(appContext, "Please Enter CIDR Larger than 15.", Toast.LENGTH_LONG)
                }  else {
                    // 150 timeout is generally what non local subnet hosts are required to scan
                    runScanAndWrite(scanner, subnet, false, 150)
                }
            } else {
                Util.popUp(appContext,"Please Enter Valid CIDR address", Toast.LENGTH_SHORT)
            }
        }

        // get the local CIDR notation so the user knows what the local scan will be
        buttonStartLocalScan.text = "Start Local Scan (${getLocalCIDR()})"

        if(getLocalCIDR() == "") {
            textLocalScan.visibility = View.INVISIBLE
            buttonStartLocalScan.visibility = View.INVISIBLE
        } else {
            textLocalScan.visibility = View.VISIBLE
            buttonStartLocalScan.visibility = View.VISIBLE
        }


        // automatically do local scan if clicked
        buttonStartLocalScan.setOnClickListener {
            (activity as SITActivity).hideSoftKeyboard()
            val localCIDR = getLocalCIDR()

            if(localCIDR == "") {
                Util.popUp(appContext,"Please connect to WiFi to use this feature!!", Toast.LENGTH_SHORT)
                textLocalScan.visibility = View.INVISIBLE
                buttonStartLocalScan.visibility = View.INVISIBLE
            } else {
                runScanAndWrite(scanner, localCIDR, true, 20)
            }
        }
    }

    private fun runScanAndWrite(scanner: NetScanner, subnet: String, isLocalScan: Boolean, timeout: Int) {
        // create coroutine otherwise will block app
        GlobalScope.launch(Dispatchers.IO) {
            val now = Date()
            val notificationId: Int = SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()

            var builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.sit_logo)
                .setContentTitle("Network Scan")
                .setContentText("Scan in progress for " + subnet)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Scan in progress for " + subnet))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, 0, false)

            val mNotificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(notificationId, builder.build())

            val openAddresses = scanner.remoteScan(subnet, timeout, appContext, builder, notificationId) // this is where scan is done

            val isAuth = FirebaseAuth.getInstance().currentUser
            // Check if user logged out during scan before saving...
            if(isAuth != null) {
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

                builder.setContentTitle("Network Scan - Complete")
                    .setContentText("Open SIT to view your scan results for " + subnet)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Open SIT to view your scan results for " + subnet)
                    )
                    .setAutoCancel(true)
                    .setProgress(100, 100, false)
                mNotificationManager.notify(notificationId, builder.build())

                Log.d(TAG, "Open hosts $openAddresses")
            } else {
                builder.setContentTitle("Network Scan - Cancelled")
                    .setContentText("Scan for " + subnet + "cancelled, user logged out of SIT.")
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Scan for " + subnet + " cancelled, user logged out of SIT."))
                    .setAutoCancel(true)
                mNotificationManager.notify(notificationId, builder.build())
            }
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
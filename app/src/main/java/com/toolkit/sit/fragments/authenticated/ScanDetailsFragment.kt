package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.toolkit.sit.R
import com.toolkit.sit.models.NetworkScanModel

class ScanDetailsFragment(private val scanData: NetworkScanModel) : Fragment(){
    private lateinit var subnetTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var resultsTextView: TextView
    private lateinit var appContext: Context
    private var TAG = "SCAN_DETAILS_FRAGMENT"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_scan_details, container, false)
        subnetTextView = view.findViewById(R.id.subnet_text)
        dateTextView = view.findViewById(R.id.created_time_text)
        resultsTextView = view.findViewById(R.id.results_text)
        appContext = view.context.applicationContext
        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Data: $scanData")
        subnetTextView.text = "Subnet: ${scanData.attemptedScan}"
        dateTextView.text = "Date: ${scanData.createdTime.toDate()}"
        resultsTextView.text = "Results: ${scanData.results}"

    }
}
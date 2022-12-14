package com.toolkit.sit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.views.NetworkScanView
import com.toolkit.sit.R
import com.toolkit.sit.fragments.authenticated.ScanDetailsFragment
import com.toolkit.sit.util.Util

class ScanAdapter(options: FirestoreRecyclerOptions<NetworkScanModel>, private val supportFragmentManager: FragmentManager)
    :FirestoreRecyclerAdapter<NetworkScanModel, NetworkScanView>(options) {
    private var TAG = "SCAN_ADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkScanView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scan, parent, false)

        return NetworkScanView(view)
    }

    override fun onBindViewHolder(holder: NetworkScanView, position: Int, model: NetworkScanModel) {
        Log.d(TAG, model::class.java.typeName.toString())
        Log.d(TAG, "View binded: $model")


        holder.createdStamp.text = "Date: ${model.createdTime.toDate()}"
        // check if shodan or not
        if (model.isNetworkScan) {
            holder.cidrView.text = "Scan: ${model.attemptedScan}"
        } else {
            when(model.scanType) {
                "SHODAN_FILTER_SEARCH" -> {
                    val info = model.attemptedScan?.split("\n")?.get(0)
                    holder.cidrView.text = "Shodan Filter Scan: ${info}"
                }
                else -> {
                    holder.cidrView.text = "Shodan Scan: ${model.attemptedScan}"
                }
            }
        }

        // each item should have a click listener that if clicked
        // will show more detailed information about the scan/shodan
        holder.button.setOnClickListener {
            Log.d(TAG, model.toString())
            Util.setFragment(supportFragmentManager, R.id.fragment_container_view, ScanDetailsFragment(model))
        }

    }

}
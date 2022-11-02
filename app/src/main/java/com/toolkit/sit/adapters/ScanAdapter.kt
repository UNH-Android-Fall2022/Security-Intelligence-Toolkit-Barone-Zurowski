package com.toolkit.sit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.toolkit.sit.models.NetworkScanModel
import com.toolkit.sit.views.NetworkScanView
import com.toolkit.sit.R

class ScanAdapter(options: FirestoreRecyclerOptions<NetworkScanModel>)
    :FirestoreRecyclerAdapter<NetworkScanModel, NetworkScanView>(options) {
    private var TAG = "SCAN_ADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkScanView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scan, parent, false)

        return NetworkScanView(view)
    }

    override fun onBindViewHolder(holder: NetworkScanView, position: Int, model: NetworkScanModel) {
        Log.d(TAG, model::class.java.typeName.toString())

        if (model.isNetworkScan) {
            // eventually have but
            Log.d(TAG, "View binded: $model")
            holder.createdStamp.text = "Date: ${model.createdTime.toDate()}"
            holder.cidrView.text = "Scan: ${model.attemptedScan}"
            holder.results.text = "Results: ${model.results}"

            holder.button.setOnClickListener {
                Log.d(TAG, model.toString())
            }
        } else {
            // TODO: is not network scan
        }
    }

}
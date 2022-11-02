package com.toolkit.sit.views

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toolkit.sit.R

class NetworkScanView(view: View) : RecyclerView.ViewHolder(view) {
    var cidrView: TextView = view.findViewById(R.id.scanCIDR)
    var createdStamp: TextView = view.findViewById(R.id.scanTimestamp)
    var results: TextView = view.findViewById(R.id.resultsScan)
    var button: Button = view.findViewById(R.id.more_info_button)
}
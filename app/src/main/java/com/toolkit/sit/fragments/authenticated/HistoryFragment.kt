package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.toolkit.sit.R
import com.toolkit.sit.adapters.ScanAdapter
import com.toolkit.sit.models.NetworkScanModel

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment(private val fragManager: FragmentManager) : Fragment() {

    private lateinit var recycleViewNetworkScan: RecyclerView
    private lateinit var scanAdapter: ScanAdapter
    private val db = FirebaseFirestore.getInstance()
    lateinit var appContext: Context

    private var TAG = "HISTORY_FRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        appContext = view.context

        // where the recycle view is located
        recycleViewNetworkScan = view.findViewById(R.id.networkView)

        // query used to get ordered scans by date and ensure correct UID
        val query: Query = db
            .collection("scans")
            .orderBy("createdTime",Query.Direction.DESCENDING)
            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid.toString())

        // setup the firestore using the NetworkScanModel
        val options = FirestoreRecyclerOptions.Builder<NetworkScanModel>()
            .setQuery(query, NetworkScanModel::class.java)
            .build()


        // setup the adapter used for each Recycler
        scanAdapter = ScanAdapter(options, fragManager)
        Log.d(TAG, scanAdapter.itemCount.toString())
        recycleViewNetworkScan.adapter = scanAdapter


        return view
    }

    override fun onStart() {
        super.onStart()
        // use linear layout
        recycleViewNetworkScan.layoutManager = LinearLayoutManager(appContext);


        scanAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        scanAdapter.stopListening()
    }
}

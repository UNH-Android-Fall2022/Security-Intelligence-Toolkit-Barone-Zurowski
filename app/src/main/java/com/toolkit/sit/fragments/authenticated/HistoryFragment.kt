package com.toolkit.sit.fragments.authenticated

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.toolkit.sit.R
import com.toolkit.sit.adapters.ScanAdapter
import com.toolkit.sit.models.NetworkScanModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

    private lateinit var recycleViewNetworkScan: RecyclerView
    private lateinit var scanAdapter: ScanAdapter
    private val db = FirebaseFirestore.getInstance()
    lateinit var appContext: Context

    private var TAG = "HISTORY_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)

        appContext = view.context
        recycleViewNetworkScan = view.findViewById(R.id.networkView)

        val query: Query = db
            .collection("scans")
            .orderBy("createdTime",Query.Direction.DESCENDING)
            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid.toString())

        val options = FirestoreRecyclerOptions.Builder<NetworkScanModel>()
            .setQuery(query, NetworkScanModel::class.java)
            .build()


        scanAdapter = ScanAdapter(options )
        Log.d(TAG, scanAdapter.itemCount.toString())
        recycleViewNetworkScan.adapter = scanAdapter


        return view
    }

    override fun onStart() {
        super.onStart()
        recycleViewNetworkScan.layoutManager = LinearLayoutManager(appContext);


        scanAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        scanAdapter.stopListening()
    }
}

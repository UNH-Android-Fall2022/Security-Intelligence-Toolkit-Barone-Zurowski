package com.toolkit.sit.models

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class NetworkScanModel(
    val createdTime: Timestamp = Timestamp.now(),
    val uid: String = "",

    val results:  List<MutableMap<String, List<Int>>>? = mutableListOf(
        hashMapOf("" to listOf())
    ),
    val attemptedScan: String? = "",
    val isLocalScan: Boolean? = false,
    val isNetworkScan: Boolean = true,
)

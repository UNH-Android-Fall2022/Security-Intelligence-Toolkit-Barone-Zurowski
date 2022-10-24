package com.toolkit.sit.models

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class NetworkScanModel(
    val createdTime: Timestamp = Timestamp.now(),
    val results:  List<MutableMap<String, List<Int>>> = mutableListOf(
        hashMapOf("" to listOf())
    ),
    val attemptedScan: String = "",
    val isLocalScan: Boolean = false,
    val uid: String = ""
)

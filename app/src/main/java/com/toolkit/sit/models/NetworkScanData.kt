package com.toolkit.sit.models

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class NetworkScanModel(
    val CreatedTime: Timestamp = Timestamp.now(),
    val Results:  List<MutableMap<String, List<Int>>> = mutableListOf(
        hashMapOf("" to listOf())
    ),
    val AttemptedScan: String = "",
    val IsLocalScan: Boolean = false,
    val uid: String = ""
)

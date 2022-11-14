package com.toolkit.sit.shodan

import com.google.gson.annotations.SerializedName

data class ShodanAPIUsageLimits(
    @SerializedName("scan_credits") val scanCredits: Int,
    @SerializedName("query_credits") val queryCredits: Int,
    @SerializedName("monitored_ips") val monitoredAddrs: Int
)

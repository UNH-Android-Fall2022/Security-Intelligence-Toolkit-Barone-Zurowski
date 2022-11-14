package com.toolkit.sit.shodan

import com.google.gson.annotations.SerializedName

data class ShodanAPIInformation(
    @SerializedName("scan_credits") val scanCredits: Int,
    @SerializedName("usage_limits") val usageLimits: ShodanAPIUsageLimits,
    val plan: String,
    val https: Boolean,
    val unlocked: Boolean,
    @SerializedName("query_credits") val queryCredits: Int,
    @SerializedName("monitored_ips") val monitoredAddrs: Int,
    @SerializedName("unlocked_left") val unlockedLeft: Int,
    val telnet: Boolean
)
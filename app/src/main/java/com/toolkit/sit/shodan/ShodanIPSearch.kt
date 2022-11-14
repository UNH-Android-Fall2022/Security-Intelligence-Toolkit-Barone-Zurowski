package com.toolkit.sit.shodan

import com.google.gson.annotations.SerializedName

// Used for store IP Search queries (e.g. 8.8.8.8)
data class ShodanIPSearch(
    @SerializedName("ip_str") val IPStr: String,
    @SerializedName("region_code") val regionCode: String?,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("country_name") val countryName: String,
    @SerializedName("dma_code") val dmaCode: String?,
    @SerializedName("last_update") val lastUpdate: String,
    @SerializedName("area_code") val areaCode: String?,
    val ip: Int,
    val city: String?,
    val isp: String,
    val latitude: String,
    val longitude: String,
    val tags: List<String>?,
    val ports: List<Int>?,
    val hostnames: List<String>?,
    val domains: List<String>?,
    val org: String,
    val asn: String,
//    val data: ShodanIPSearchData
)


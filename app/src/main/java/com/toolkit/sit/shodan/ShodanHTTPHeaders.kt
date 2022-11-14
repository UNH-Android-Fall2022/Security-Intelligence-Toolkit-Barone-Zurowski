package com.toolkit.sit.shodan


import com.google.gson.annotations.SerializedName;

// Data class for when user queries the Shodan API to check their HTTP Headers.
data class ShodanHTTPHeaders(
    @SerializedName("Content-Length") val length: String,
    @SerializedName("Cf-Visitor") val visitor: String,
    @SerializedName("Accept-Encoding") val encoding: String,
    @SerializedName("Host") val host: String,
    @SerializedName("Cf-Request-Id") val requestId: String,
    @SerializedName("User-Agent") val userAgent: String,
    @SerializedName("Connection") val connection: String,
    @SerializedName("X-Forwarded-Proto") val forwardedProto: String,
    @SerializedName("Accept") val accept: String,
    @SerializedName("Cdn-Loop") val cdnLoop: String,
    @SerializedName("Cf-Connecting-Ip") val connectingIP: String,
    @SerializedName("Cf-Ray") val ray: String,
    @SerializedName("Content-Type") val contentType: String
)

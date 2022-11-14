package com.toolkit.sit.shodan

import android.util.Log
import com.toolkit.sit.util.Util
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

// This class Observes HTTP Requests sent to the API
// What this does is automatically chain the API key to each request to make it less redundant.
internal class ShodanInterceptor : Interceptor {

    val TAG = "SHODAN_INTERCEPTOR"
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url: HttpUrl = request.url().newBuilder()
            .addQueryParameter("key", Util.shodanAPIKey)
            .build()
        val newRequest = request.newBuilder().url(url).build()
        Log.d(TAG, newRequest.url().toString())
        return chain.proceed(newRequest)
    }
}
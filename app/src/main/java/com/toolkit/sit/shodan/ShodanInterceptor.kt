package com.toolkit.sit.shodan

import com.toolkit.sit.util.Util
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class ShodanInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url: HttpUrl = request.url().newBuilder()
            // api key hardcoded for now
            .addQueryParameter("key", Util.shodanAPIKey)
            .build()
        val newRequest = request.newBuilder().url(url).build()
        println(newRequest.url().toString())
        return chain.proceed(newRequest)
    }
}
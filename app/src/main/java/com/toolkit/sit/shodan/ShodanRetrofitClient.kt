package com.toolkit.sit.shodan

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ShodanRetrofitClient {
    companion object {
        private val shodanURL = "https://api.shodan.io"
        private var shodanAPI: ShodanAPI? = null

        @Synchronized
        fun getInstance(): ShodanAPI {
            if(shodanAPI == null) {
                // add the interceptor to dynamically add API Key to EACH REQUEST
                val client = OkHttpClient.Builder()
                    .addInterceptor(ShodanInterceptor())
                    .build()


                val retrofit = Retrofit.Builder().baseUrl(shodanURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

                // Creates interface to interact with API
                shodanAPI = retrofit.create(ShodanAPI::class.java)

            }
            return shodanAPI as ShodanAPI
        }
    }
}
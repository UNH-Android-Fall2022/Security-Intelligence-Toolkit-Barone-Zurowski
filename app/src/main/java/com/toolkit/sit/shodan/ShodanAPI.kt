package com.toolkit.sit.shodan

import com.toolkit.sit.util.Util
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShodanAPI {

    @GET("/shodan/host/{ip}")
    suspend fun searchIP(@Path(value="ip")ip: String): ShodanIPSearch

    @GET("/shodan/host/search/filters")
    suspend fun getFilters(): List<String>

    @GET("/tools/myip")
    suspend fun getPublicIPv4Addr(): String

    @GET("/tools/httpheaders")
    suspend fun getHTTPHeaders(): ShodanHTTPHeaders

    @GET("/api-info")
    suspend fun getAPIInfo(): ShodanAPIInformation

    @GET("/shodan/query/search")
    suspend fun search(@Query("query") query: String): ShodanSearch

    @GET("/shodan/query/tags")
    suspend fun searchTags(): ShodanSearch

}

//class Main
// click run icon to test.
fun main() {
    val apiKey = "<REDACTED_FOR_NOW>"
    Util.setShodanKey(apiKey)
    val shodanAPI: ShodanAPI = ShodanRetrofitClient.getInstance()

    runBlocking {
        launch {
            println(shodanAPI.getFilters())
            println(shodanAPI.getHTTPHeaders())
            println(shodanAPI.getPublicIPv4Addr())
            println(shodanAPI.getAPIInfo())
            println(shodanAPI.search("webcam"))
            println(shodanAPI.searchTags())
            println(shodanAPI.searchIP("8.8.8.8"))
        }
    }

}

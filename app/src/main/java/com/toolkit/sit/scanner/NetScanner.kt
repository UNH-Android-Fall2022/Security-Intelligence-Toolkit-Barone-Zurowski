package com.toolkit.sit.scanner

import android.os.Build
import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.util.SubnetUtils
import java.io.IOException
import java.net.*

class NetScanner {
    public fun remoteScan(cidr: String): Int {
        val ips = getIPs(cidr)
        val ports = listOf<Int>(80, 443)

        ips.forEach { ip ->
            ports.forEach { port ->
                doTCPCheck(ip, port)
            }
        }

        return 0
    }

    private fun doTCPCheck(ip: InetAddress, port: Int): Boolean {

        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        var socket: Socket? = null
        try {
            Log.d("SCANNER", "Attempting socket: $ip : $port")
            socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 1000)
            return true
        } catch (ex: IOException) {
            Log.d("SCANNER", "Error: $ex")
            return false
        } catch (ex: SocketTimeoutException) {
            Log.d("SCANNER", "Error: $ex")
            return false
        } finally {
            socket?.close()
        }
    }

    private fun getIPs(cidr: String): List<InetAddress> =
        SubnetUtils(cidr).info
            .allAddresses
            .toList()
            .map { InetAddress.getByName(it) }
}
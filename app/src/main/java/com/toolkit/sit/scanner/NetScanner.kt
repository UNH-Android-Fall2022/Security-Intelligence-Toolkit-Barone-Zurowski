package com.toolkit.sit.scanner

import android.os.Build
import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.net.util.SubnetUtils
import java.io.IOException
import java.net.*

class NetScanner {
    private var TAG = "NET_SCANNER"
    suspend fun remoteScan(cidr: String): List<Pair<String, List<Int>>> {

        val ips: List<InetAddress> = if(cidr.split("/")[1] == "32") {
            listOf(InetAddress.getByName(cidr.split("/")[0]))
        } else {
            getIPs(cidr)
        }

        val openAddresses = mutableListOf<Pair<String, List<Int>>>()

        val ports = listOf(80, 443)

        ips.forEach { ip ->
            val portsOpen = mutableListOf<Int>()
            ports.forEach { port ->
                coroutineScope {
                    launch {
                        if(doTCPCheck(ip, port)) {
                           portsOpen.add(port)
                        }
                    }
                }
            }

            if(portsOpen.size > 0) {
                openAddresses.add(Pair(ip.hostAddress, portsOpen))
            }
        }

        return openAddresses
    }

    private fun doTCPCheck(ip: InetAddress, port: Int): Boolean {
        var socket: Socket? = null
        return try {
            Log.d(TAG, "Attempting socket: ${ip.hostAddress} : $port")
            socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 150)
            true
        } catch (ex: IOException) {
            Log.d(TAG, "Error: $ex")
            false
        } catch (ex: SocketTimeoutException) {
            Log.d(TAG, "Error: $ex")
            false
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
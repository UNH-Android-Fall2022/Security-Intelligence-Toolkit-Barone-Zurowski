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
    suspend fun remoteScan(cidr: String, timeout: Int): List<MutableMap<String, List<Int>>> {

        // check if /32 create list of only one IP.
        val ips: List<InetAddress> = if(cidr.split("/")[1] == "32") {
            listOf(InetAddress.getByName(cidr.split("/")[0]))
        } else {
            // if CIDR is < 32 create list of IPs
            getIPs(cidr)
        }

        // results for addresses
        val openAddresses = mutableListOf<MutableMap<String, List<Int>>>()

        // current ports that it will attempt to connec tto.
        val ports = listOf(80, 443)

        ips.forEach { ip ->
            val portsOpen = mutableListOf<Int>()
            ports.forEach { port ->
                // create a routine to do a tcp connect scan and if it works add the applicable
                coroutineScope {
                    launch {
                        if(doTCPCheck(ip, port, timeout)) {
                           portsOpen.add(port)
                        }
                    }
                }
            }

            // if there is more than one port open add to map.
            if(portsOpen.size > 0) {
                val openPortsMap: MutableMap<String, List<Int>> = HashMap()
                openPortsMap[ip.hostAddress] = portsOpen
                openAddresses.add(openPortsMap)
            }
        }

        return openAddresses
    }

    private fun doTCPCheck(ip: InetAddress, port: Int, timeout: Int): Boolean {
        var socket: Socket? = null
        return try {
            Log.d(TAG, "Attempting socket: ${ip.hostAddress} : $port")
            socket = Socket()
            // connect to IP:PORT via TCP
            socket.connect(InetSocketAddress(ip, port), timeout)
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

    // used to get IPs to a list via string CIDR notation
    private fun getIPs(cidr: String): List<InetAddress> =
        SubnetUtils(cidr).info
            .allAddresses
            .toList()
            .map { InetAddress.getByName(it) }
}
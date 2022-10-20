package com.toolkit.sit.scanner

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.commons.net.util.SubnetUtils
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class NetScanner {
    public fun remoteScan(cidr: String): Int {
        val ips = getIPs(cidr)
        val ports = listOf<Int>(80, 443)
        val sockAddrs = ips
            .filter {
                it.isReachable(1000)
            }
            .flatMap {
                genAddrs(it, ports)
            }

        runBlocking {
            sockAddrs
                .map {
                    async {
                        doScan(it, 1000)
                    }
                }
                .map {
                    it.await()
                }
                .filter {
                    it.second
                }
                .map { it.first }
                .forEach {
                    Log.d("SCANNER", it)
                }
        }
        return 0
    }

    private suspend fun doScan(addr: InetSocketAddress, timeout: Int): Pair<String, Boolean> = withContext(Dispatchers.IO) {
        val sock = Socket()
        kotlin.runCatching { sock.connect(addr, timeout) }
            .map { "${addr.hostString}:${addr.port}" to true}
            .getOrElse { "${addr.hostString}:${addr.port}" to false}
    }

    private fun getIPs(cidr: String): List<InetAddress> =
        SubnetUtils(cidr).info
            .allAddresses
            .toList()
            .map { InetAddress.getByName(it) }

    private fun genAddrs(ip: InetAddress, ports: List<Int>): List<InetSocketAddress> =
        ports.map { port ->
            InetSocketAddress(ip, port)
        }

}
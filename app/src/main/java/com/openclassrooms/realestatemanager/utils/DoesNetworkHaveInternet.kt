package com.openclassrooms.realestatemanager.utils

import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object DoesNetworkHaveInternet {
    fun execute(): Boolean {
        return try {
            Timber.d("Pinging google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 15000)
            socket.close()
            Timber.d("Ping Success.")
            true
        } catch (e: IOException) {
            Timber.d("No Internet connection. $e")
            false
        }
    }
}

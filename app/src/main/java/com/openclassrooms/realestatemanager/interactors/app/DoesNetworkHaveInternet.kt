package com.openclassrooms.realestatemanager.interactors.app

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object DoesNetworkHaveInternet {
    private const val TAG: String = "socketFactory"

    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            Log.d(TAG, "execute: PINGING GOOGLE")
            val socket = socketFactory.createSocket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d(TAG, "execute: PING SUCCESS")
            true
        } catch (e: IOException) {
            Log.e(TAG, "execute: No Internet connection $e")
            false
        }
    }
}
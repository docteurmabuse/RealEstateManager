package com.openclassrooms.realestatemanager.utils

import android.util.Log
import com.openclassrooms.realestatemanager.utils.Utils.TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object DoesNetworkHaveInternet {
    fun execute(): Boolean {
        return try {
            Log.d(TAG, "execute: PINGING GOOGLE")
            val socket = Socket()
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
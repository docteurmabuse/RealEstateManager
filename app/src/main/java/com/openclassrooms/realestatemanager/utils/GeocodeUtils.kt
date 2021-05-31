package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import java.io.IOException

object GeocodeUtils {

    fun getLatLngFromAddress(address: String, context: Context): LatLng? {
        val geocoder = Geocoder(context)
        val resultAddresses: List<Address>?
        val resultAddress: Address?
        var latLng: LatLng? = null

        try {
            resultAddresses = geocoder.getFromLocationName(address, 1)
            // 3
            if (null != resultAddresses && resultAddresses.isNotEmpty()) {
                resultAddress = resultAddresses[0]
                if (resultAddress.maxAddressLineIndex > -1) {
                    latLng = LatLng(resultAddress.latitude, resultAddress.longitude)
                }
            }
        } catch (e: IOException) {
            Timber.e("MapsActivity: ${e.localizedMessage}")
        }

        return latLng
    }
}

package com.openclassrooms.realestatemanager.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import timber.log.Timber

fun setUpPermissionsUtil(context: Context): Boolean {
    var isPermissionsAllowed: Boolean = false
    Dexter.withContext(context)
        .withPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    isPermissionsAllowed = report.areAllPermissionsGranted()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        })
        .withErrorListener {
            Timber.d(it.name)
        }
        .check()
    return isPermissionsAllowed
}

/**
 * Function to request permission from the user
 */
fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        requestId
    )
}

/**
 * Function to check if the location permissions are granted or not
 */
fun isAccessFineLocationGranted(context: Context): Boolean {
    return ContextCompat
        .checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Function to check if location of the device is enabled or not
 */
fun isLocationEnabled(context: Context): Boolean {
    val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

/**
 * Function to show the "enable GPS" Dialog box
 */
fun showGPSNotEnabledDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.enable_gps))
        .setMessage(context.getString(R.string.required_for_this_app))
        .setCancelable(false)
        .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        .show()
}

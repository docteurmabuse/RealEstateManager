package com.openclassrooms.realestatemanager.presentation.ui.property_map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.openclassrooms.realestatemanager.R

class MapFragment : Fragment() {
    private var mapView: MapView? = null
    private lateinit var mapboxMap: MapboxMap

    companion object {
        private const val REQUEST_LOCATION = 1
    }

    lateinit var permissionsManager: PermissionsManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        setupLocationClient()
        return inflater.inflate(R.layout.map_layout, container, false)
    }

    private fun setupLocationClient() {
        var locationEngine = LocationEngineProvider.getBestLocationEngine(requireActivity())

        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

// Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location


        } else {
            permissionsManager = PermissionsManager(this.permissionsListener)
            permissionsManager.requestLocationPermissions(requireActivity())
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    var permissionsListener: PermissionsListener = object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: List<String>) {

        }

        override fun onPermissionResult(granted: Boolean) {
            if (granted) {

                // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

            } else {

                // User denied the permission

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById<MapView>(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { it ->
            mapboxMap = it
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                enableLocationComponent(style)

            }

        }
    }

    private fun enableLocationComponent(style: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component
            val locationComponent = mapboxMap.locationComponent
            val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(Color.MAGENTA)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()

            // Activate with a built LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    requireContext(),
                    style
                ).build()
            )

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS

        } else {

            permissionsManager = PermissionsManager(this.permissionsListener)

            permissionsManager.requestLocationPermissions(requireActivity())

        }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}
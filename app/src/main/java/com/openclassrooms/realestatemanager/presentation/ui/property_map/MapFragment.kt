package com.openclassrooms.realestatemanager.presentation.ui.property_map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MapFragment constructor(private var properties: List<Property>) : Fragment(),
    GoogleMap.OnInfoWindowClickListener {

    private var lastLocation: Location? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by viewModels()
    private var isRestore = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchProperties()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        isRestore = savedInstanceState != null
        return inflater.inflate(R.layout.map_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        lifecycleScope.launchWhenCreated {
            // Get map
            if (mapFragment != null) {
                googleMap = mapFragment.awaitMap()
            }

            // Wait for map to finish loading
            googleMap.awaitMapLoad()
            googleMap.setOnInfoWindowClickListener { marker ->
                onInfoWindowClick(marker)
            }
            googleMap.uiSettings.isZoomControlsEnabled = true
            getLastKnownLocation()
            if (!isRestore) {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(40.714327, -73.869851),
                        10f
                    )
                )
            }
            if (lastLocation != null) {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastLocation!!.latitude,
                            lastLocation!!.longitude
                        ), 12f
                    )
                )
            } else {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            40.714327, -73.869851
                        ), 12f
                    )
                )
            }
            setObserver()
        }
    }

    private fun getLastKnownLocation() {
        Timber.d("getLastKnown Location called")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        googleMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
    }

    private fun viewPropertyDetail(property: Property) {
        val navHostFragment = findNavController()
        val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToPropertyDetailFragment(
            true, property
        )
        navHostFragment.navigate(action)
    }


    private fun setObserver() {
        lifecycleScope.launch {
            val value = viewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { properties ->
                            renderList(properties)
                        }
                    }
                    DataState.Status.LOADING -> {

                    }
                    DataState.Status.ERROR -> {
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
    }

    private fun renderList(list: List<Property>) {
        properties = list


        Timber.tag("MAP").d("MAP_PROPERTIES: ${properties.size}")
        properties.forEach { property ->
            if (property.address?.lat != null && property.address?.lng != null) {
                val location = LatLng(property.address?.lat!!, property.address?.lng!!)
                addMarkers(googleMap, location, property)
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap, location: LatLng, property: Property) {
        val marker: Marker? = googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(property.address?.address1)
        )
        marker?.tag = property
    }

    override fun onInfoWindowClick(marker: Marker) {
        Timber.d("MARKER_CLICK ok")
        viewPropertyDetail(marker.tag as Property)
    }

}

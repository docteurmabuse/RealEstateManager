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
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragment
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MapFragment constructor(private var properties: List<Property>) : Fragment() {

    private var lastLocation: Location? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: PropertyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchProperties()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        return inflater.inflate(R.layout.map_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

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
                    // placeMarkerOnMap(currentLatLng)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        map.uiSettings.isZoomControlsEnabled
        getLastKnownLocation()
        if (lastLocation != null) {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        lastLocation!!.latitude,
                        lastLocation!!.longitude
                    ), 12f
                )
            )
        }
        setObserver()
        googleMap.setOnInfoWindowClickListener { marker ->
            viewPropertyDetail(marker.tag as Property?)
        }
    }

    private fun viewPropertyDetail(tag: Property?) {
        val bundle = Bundle()
        bundle.putParcelable(
            PropertyDetailFragment.ARG_PROPERTY,
            tag
        )
        val itemDetailFragmentContainer: View? =
            view?.findViewById(R.id.item_detail_nav_container)
        itemDetailFragmentContainer?.findNavController()
            ?.navigate(R.id.propertyDetailFragmentWide, bundle)
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
                var location: LatLng = LatLng(property.address?.lat!!, property.address?.lng!!)
                addMarkers(googleMap, location, property)
                /*  val marker = googleMap.addMarker(
                      MarkerOptions()
                          .title(property.address!!.address1)
                          .position()
                  )*/
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap, location: LatLng, property: Property) {
        val marker: Marker? = googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(property.address?.address1)
        )
        marker?.tag = property
    }

}

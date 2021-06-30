package com.openclassrooms.realestatemanager.presentation.ui.property_map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MapLayoutBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@AndroidEntryPoint
class MapFragment :
    Fragment(R.layout.map_layout),
    GoogleMap.OnInfoWindowClickListener {
    private var properties: List<Property> = arrayListOf()
    private var lastLocation: Location? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: LatLng = LatLng(40.714327, -73.869851)
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: MapLayoutBinding? = null
    private val binding get() = _binding!!
    private var isRestore = false
    private var isPermissionsAllowed = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapLayoutBinding.inflate(inflater, container, false)

        isRestore = savedInstanceState != null
        requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar).performShow()
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        lastLocation = location
                    }
                }
            },
            Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        isPermissionsAllowed = report.areAllPermissionsGranted()
                        setUpLocationListener()
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
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()

    }


    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar).performShow()
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("PotentialBehaviorOverride")
    private fun setupMap() {
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
        googleMap.isMyLocationEnabled = true
        if (isPermissionsAllowed) {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation,
                    12f
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

    }

    private fun viewPropertyDetail(property: Property) {
        val navHostFragment = findNavController()
        val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToPropertyDetailFragment(
            true, property
        )
        navHostFragment.navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            val value = viewModel.stateFilter
            value.collectLatest {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        displayLoading(false)
                        it.data?.let { properties -> renderList(properties) }
                    }
                    DataState.Status.LOADING -> {
                        displayLoading(true)
                    }
                    DataState.Status.ERROR -> {
                        displayLoading(false)
                        displayError(it.message)
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
    }

    private fun displayLoading(isLoading: Boolean) {
        if (isLoading) binding.mapProgressBar.visibility = View.VISIBLE
        else binding.mapProgressBar.visibility = View.GONE
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderList(list: List<Property>) {
        properties = list
        Timber.tag("MAP").d("MAP_PROPERTIES in Map: ${properties.size}")
        googleMap.clear()
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

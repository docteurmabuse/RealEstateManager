package com.openclassrooms.realestatemanager.presentation.ui.property_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MapFragment constructor(private var properties: List<Property>) : Fragment() {

    private lateinit var googleMap: GoogleMap

    private val viewModel: PropertyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchProperties()
        return inflater.inflate(R.layout.map_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setObserver()
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
                addMarkers(googleMap, location)
                /*  val marker = googleMap.addMarker(
                      MarkerOptions()
                          .title(property.address!!.address1)
                          .position()
                  )*/
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap, location: LatLng) {
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}

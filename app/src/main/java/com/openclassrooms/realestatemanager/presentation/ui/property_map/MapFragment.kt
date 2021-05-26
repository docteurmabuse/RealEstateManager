package com.openclassrooms.realestatemanager.presentation.ui.property_map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.*
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MapFragment constructor(private var properties: List<Property>) : Fragment(),
    OnMapReadyCallback, PermissionsListener {

    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mapboxMap: MapboxMap
    private var mapView: MapView? = null
    private val viewModel: PropertyListViewModel by viewModels()
    private var symbolManager: SymbolManager? = null
    private val symbol: Symbol? = null
    private var symbolLayerIconPropertyList = arrayListOf<List<Property>>()
    private val ID_ICON_AIRPORT = "airport"
    private val MAKI_ICON_CAR = "car-15"
    private val MAKI_ICON_CAFE = "cafe-15"
    private val MAKI_ICON_CIRCLE = "fire-station-15"
    private val ID_ICON_PINK = "pink-24"
    private var symbolOptionsList: ArrayList<SymbolOptions> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        viewModel.fetchProperties()
        return inflater.inflate(R.layout.map_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById<MapView>(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            mapboxMap.uiSettings.isZoomGesturesEnabled = true
            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            enableLocationComponent(it)
            addIconImageToStyle(it)
            setSymbolManager(it)
            //    initMarkerSymbolLayer(it)
        }
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

        for (property in properties) {
            addSymbol(property)
        }
        symbolManager?.create(symbolOptionsList)
        Timber.tag("MAP").d("MAP_PROPERTIES: ${properties.size}")
        Timber.d("MARKER: $symbolOptionsList")
    }

    private fun addSymbol(property: Property) {
// Create a symbol at the specified location.

        var latitude = property.address?.lat
        var longitude = property.address?.lng
        if (latitude != null && longitude != null) {
            symbolOptionsList.add(
                SymbolOptions().withLatLng(LatLng(latitude, longitude))
                    .withIconImage(ID_ICON_AIRPORT)
            )
        }
    }

    private fun addIconImageToStyle(style: Style) {
        style.addImage(
            ID_ICON_PINK,
            BitmapUtils.getBitmapFromDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_mapbox_marker_icon_pink
                )
            )!!,
            true
        )
    }

    private fun initMarkerSymbolLayer(style: Style?) {
        if (style != null) {
            BitmapUtils.getBitmapFromDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_mapbox_marker_icon_pink
                )
            )
                ?.let {
                    style.addImage(
                        "marker_icon_pink-id",
                        it
                    )
                }
            style.addSource(GeoJsonSource("source-id"))
            style.addLayer(
                SymbolLayer("layer-id", "source-id").withProperties(
                    iconImage("marker_icon_pink-id"),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true),
                    iconSize(.7f)
                )
            )
        }

    }

    private fun setSymbolManager(style: Style) {// create symbol manager
        // create symbol manager
        val geoJsonOptions = GeoJsonOptions().withTolerance(0.4f)
        symbolManager = SymbolManager(mapView!!, mapboxMap, style, null, geoJsonOptions)
        symbolManager!!.addClickListener(OnSymbolClickListener { symbol: Symbol ->
            Toast.makeText(
                requireContext(), String.format("Symbol clicked %s", symbol.id),
                Toast.LENGTH_SHORT
            ).show()
            false
        })
        symbolManager!!.addLongClickListener(OnSymbolLongClickListener { symbol: Symbol ->
            Toast.makeText(
                requireContext(), String.format("Symbol long clicked %s", symbol.id),
                Toast.LENGTH_SHORT
            ).show()
            false
        })

        // set non data driven properties

        // set non data driven properties
        symbolManager!!.iconAllowOverlap = true
        symbolManager!!.textAllowOverlap = true
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            // Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {

                // Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

                // Enable to make the LocationComponent visible
                isLocationComponentEnabled = true

                // Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

                // Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            requireContext(),
            R.string.user_location_permission_explanation,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(
                requireContext(),
                R.string.user_location_permission_not_granted,
                Toast.LENGTH_LONG
            ).show()
            requireActivity().finish()
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

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}
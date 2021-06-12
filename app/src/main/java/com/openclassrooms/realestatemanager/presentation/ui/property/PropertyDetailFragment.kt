package com.openclassrooms.realestatemanager.presentation.ui.property

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.nambimobile.widgets.efab.ExpandableFabLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyDetailBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyPagerAdapter
import com.openclassrooms.realestatemanager.utils.EDIT_PROPERTY_VIEW
import com.openclassrooms.realestatemanager.utils.MAPVIEW_BUNDLE_KEY
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@AndroidEntryPoint
class PropertyDetailFragment : Fragment(R.layout.property_detail) {


    private var property: Property? = null
    private var viewPager: ViewPager2? = null
    private var dotsIndicator: DotsIndicator? = null
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private var adapter = PropertyPagerAdapter()
    private var _binding: PropertyDetailBinding? = null
    private var liteMap: GoogleMap? = null
    private val viewModel: PropertyDetailViewModel by viewModels()
    private lateinit var mapView: MapView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            //   property = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle == null) {
            Timber.d("PropertyDetailFragment did not received arguments")
            return
        }
        val args = PropertyDetailFragmentArgs.fromBundle(bundle)
        property = args.property
        Timber.d("PROPERTY_DETAIL: $property")
        property?.agent?.let { viewModel.start(it) }
        setObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PropertyDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        toolbarLayout = binding.toolbarLayout
        setupViewPager()
        updateContent()
        rootView.setOnDragListener(dragListener)
        setFabListener()

        requireActivity().findViewById<ExpandableFabLayout>(R.id.expandable_fab_layout)
            .removeAllViews()
        return rootView
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        dotsIndicator = binding.dotsIndicator
        viewPager!!.adapter = adapter
        dotsIndicator!!.setViewPager2(viewPager!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById<MapView>(R.id.lite_map_view)
        initGoogleMap(savedInstanceState)
    }

    private fun setFabListener() {
        binding.addPropertyFAB?.setOnClickListener {
            val navHostFragment = findNavController()
            val action = property?.let { property ->
                PropertyDetailFragmentDirections.actionPropertyDetailFragmentToAddPropertyFragment(
                    EDIT_PROPERTY_VIEW,
                    property
                )
            }

            if (action != null) {
                navHostFragment.navigate(action)
            }
            Timber.tag("PROPERTY").d("PROPERTY_ID:")
        }
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            val value = viewModel.agentState
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { agent -> renderAgent(agent) }
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

    private fun renderAgent(agent: Agent) {
        binding.agent = agent
    }


    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        lifecycleScope.launchWhenCreated {
            // Get map
            liteMap = mapView.awaitMap()
            // Get map
            liteMap!!.awaitMapLoad()
            liteMap!!.uiSettings.isZoomControlsEnabled = true
            if (property?.address?.lat != null && property?.address?.lng != null) {
                val location = LatLng(property!!.address?.lat!!, property!!.address?.lng!!)
                liteMap!!.moveCamera(CameraUpdateFactory.newLatLng(location))
                addMarker(liteMap, location)
            }
        }
    }

    private fun addMarker(googleMap: GoogleMap?, location: LatLng) {
        googleMap?.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .title(property?.address?.address1)
        )
    }

    private fun updateContent() {
        //toolbarLayout?.title = property?.content
        lifecycleScope.launchWhenStarted {
            // Show the placeholder content as text in a TextView.
            property?.let {
                binding.property = it
                adapter.submitList(it.media.photos)
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
        const val ARG_PROPERTY = "property"

    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewPager!!.adapter = null
    }
}

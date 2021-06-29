package com.openclassrooms.realestatemanager.presentation.ui.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyDetailBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyPagerAdapter
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
    private val maiViewModel: MainViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private var isCurrencyEuro = false
    private var itemDetailFragmentContainer: View? = null
    private val binding get() = _binding!!
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(
                R.id.itemTabsFragment2
            )
        }
    }
   /* private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            //   property = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            if (it.containsKey(ARG_PROPERTY_ID)) {
                it.getParcelable<Property>(ARG_PROPERTY_ID)?.let { it1 ->
                    this.property = it1
                    updateContent()
                    property?.agent?.let {
                        viewModel.getAgent(it)
                        setAgentObserver()
                    }
                    setPropertyObserver()
                    setAgentObserver()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PropertyDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        binding.lifecycleOwner = this.viewLifecycleOwner
        //  rootView.setOnDragListener(dragListener)
        toolbarLayout = binding.toolbarLayout
        setupViewPager()
        setCurrencyListener()
        return rootView
    }

    private fun setCurrencyListener() {
        maiViewModel.isEuroCurrency.observe(viewLifecycleOwner) {
            it?.let {
                this.isCurrencyEuro = it
                binding.isCurrencyEuro = it
            }
        }
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        dotsIndicator = binding.dotsIndicator
        viewPager!!.adapter = adapter
        dotsIndicator!!.setViewPager2(viewPager!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (property == null) {
            binding.noPropertyText?.visibility = View.VISIBLE
            binding.appBar.visibility = View.GONE
            binding.itemDetailScrollView.visibility = View.GONE
        }
        itemDetailFragmentContainer =
            requireActivity().findViewById(R.id.item_detail_nav_container)
        setFabListener(view)
        mapView = view.findViewById<MapView>(R.id.lite_map_view)
        initGoogleMap(savedInstanceState)
    }

    private fun setFabListener(view: View) {
        binding.addPropertyFAB.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(
                ARG_PROPERTY_ID,
                property
            )
            if (itemDetailFragmentContainer != null) {
                requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.addEditPropertyFragment, bundle)

            } else {
                view.findNavController().navigate(R.id.addEditPropertyFragment, bundle)
            }
        }
    }

    private fun setAgentObserver() {
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

    private fun setPropertyObserver() {
        lifecycleScope.launchWhenStarted {
            val value = viewModel.propertyState
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { property -> renderProperty(property) }
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

    private fun renderProperty(property: Property) {
        this.property = property
        binding.property = property
        property.agent?.let { viewModel.getAgent(it) }
        updateContent()
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
        lifecycleScope.launchWhenStarted {
            Timber.d("PROPERTY_DETAIL=$property")

            property?.let {
                binding.property = it
                adapter.submitList(it.media.photos)
                toolbarLayout?.title = property?.address?.address1
            }

        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_PROPERTY_ID = "property"
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        callback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewPager!!.adapter = null
        viewPager = null
        dotsIndicator = null
        mapView.onDestroy()
    }
}

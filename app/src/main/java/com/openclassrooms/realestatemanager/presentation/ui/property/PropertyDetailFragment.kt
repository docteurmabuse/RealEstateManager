package com.openclassrooms.realestatemanager.presentation.ui.property

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyDetailBinding
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
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
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                property = it.getParcelable(ARG_PROPERTY)
                Timber.d("PROPERTY_DETAIL: $property")
            }
        }

    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        dotsIndicator = binding.dotsIndicator
        viewPager!!.adapter = adapter
        dotsIndicator!!.setViewPager2(viewPager!!)

        //tabLayout = binding.tabLayout
        // tabLayout!!.setupr(viewPager, true)

//        tabLayout?.let {
//            TabLayoutMediator(it, viewPager!!) { tab, position ->
//                tab.text = "OBJECT ${(position + 1)}"
//            }.attach()
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        liteMap = googleMap ?: return
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(40.0, 40.0))
                .title("Marker")
        )
        Timber.d("LITE_MAP_CALL: $property")

    }*/
    private fun setupLiteMap() {

    }

    private val callback = OnMapReadyCallback { map ->
        liteMap = map
        MapsInitializer.initialize(requireContext())
        map.uiSettings.isZoomControlsEnabled = true
        //addMarkers()
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        Timber.d("LITE_MAP_CALL: $property")
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

        return rootView
    }


    private fun addMarkers() {
        if (property?.address?.lat != null && property?.address?.lng != null) {
            val location = LatLng(property?.address?.lat!!, property?.address?.lng!!)
            liteMap?.addMarker(
                MarkerOptions().position(location).title(property?.address?.address1)
            )
            liteMap?.moveCamera(CameraUpdateFactory.newLatLng(location))
        }
        Timber.d("LITE_MAP: $property")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewPager!!.adapter = null
    }
}

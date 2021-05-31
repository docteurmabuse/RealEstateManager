package com.openclassrooms.realestatemanager.presentation.ui.property_list

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nambimobile.widgets.efab.ExpandableFab
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyListBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class PropertyListFragment constructor(private var properties: List<Property>) :
    Fragment(R.layout.property_list) {

    private var columnCount = 1
    private var _binding: PropertyListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PropertyListViewModel by viewModels()
    private lateinit var adapter: PropertyAdapter
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchProperties()
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        setUpMedia()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PropertyListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.propertyList
        setObserver()
        viewModel.fetchProperties()
        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? =
            view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Property
            val bundle = Bundle()
            bundle.putParcelable(
                PropertyDetailFragment.ARG_PROPERTY,
                item
            )

            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.propertyDetailFragmentWide, bundle)
            } else {
                itemView.findNavController().navigate(R.id.propertyDetailFragment, bundle)
            }


            val expandableFabPortrait =
                view.findViewById<ExpandableFab>(R.id.expandable_fab_portrait)
            val expandableFabLandscape =
                view.findViewById<ExpandableFab>(R.id.expandable_fab_landscape)
            val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)

            if (expandableFabPortrait.visibility == View.VISIBLE) {
                (expandableFabPortrait.layoutParams as CoordinatorLayout.LayoutParams).anchorId =
                    bottomAppBar.id
                (expandableFabLandscape.layoutParams as CoordinatorLayout.LayoutParams).anchorId =
                    View.NO_ID
            } else {
                (expandableFabPortrait.layoutParams as CoordinatorLayout.LayoutParams).anchorId =
                    View.NO_ID
                (expandableFabLandscape.layoutParams as CoordinatorLayout.LayoutParams).anchorId =
                    bottomAppBar.id
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as Property
            Toast.makeText(
                v.context,
                "Context click of item " + item.id,
                Toast.LENGTH_LONG
            ).show()
            true
        }

        setupRecyclerView(recyclerView!!, onClickListener, onContextClickListener)

        setFabListener()
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {
        adapter = PropertyAdapter(
            onClickListener,
            onContextClickListener
        )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setFabListener() {
        binding.addPropertyFAB?.setOnClickListener {
            val navHostFragment = findNavController()
            val newPropertyId = properties.size.toLong() + 1
            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddPropertyFragment(
                newPropertyId, false, null
            )

            navHostFragment.navigate(action)
            Timber.tag("PROPERTY_ID").d("PROPERTY_ID: $newPropertyId")
        }
    }

    private fun setObserver() {
        lifecycleScope.launchWhenResumed {
            val value = viewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { properties -> renderList(properties) }
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

    private fun setUpMedia() {
        Dexter.withContext(requireContext())
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
                        if (report.areAllPermissionsGranted()) {
                            Timber.d("PERMISSIONS OK")
                        }
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

        /* if (haveStoragePermission()) {
             showImages()
         } else {
             requestPermission()
         }*/
    }

    private fun renderList(list: List<Property>) {
        adapter.submitList(list)
        properties = list
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        /*  @JvmStatic
          fun newInstance(columnCount: Int) =
              PropertyListFragment(properties).apply {
                  arguments = Bundle().apply {
                      putInt(ARG_COLUMN_COUNT, columnCount)
                  }
              }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        recyclerView!!.adapter = null
    }
}

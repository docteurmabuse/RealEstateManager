package com.openclassrooms.realestatemanager.presentation.ui.property_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyListBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class PropertyListFragment : Fragment(R.layout.property_list) {

    private var columnCount = 1
    private var _binding: PropertyListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PropertyListViewModel by viewModels()
    private lateinit var adapter: PropertyAdapter
    private var properties: List<Property> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchProperties()
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
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
        val recyclerView: RecyclerView = binding.propertyList
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

        setupRecyclerView(recyclerView, onClickListener, onContextClickListener)

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
        binding.addPropertyFAB.setOnClickListener {
            val navHostFragment = findNavController()
            val newPropertyId = properties.size.toLong() + 1
            val action =
                PropertyListFragmentDirections.actionPropertyListFragmentToAddPropertyFragment(
                    newPropertyId
                )
            navHostFragment.navigate(action)
            Timber.tag("PROPERTYID").d("PROPERTYID: $newPropertyId")
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
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

    private fun renderList(list: List<Property>) {
        adapter.submitList(list)
        properties = list
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PropertyListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
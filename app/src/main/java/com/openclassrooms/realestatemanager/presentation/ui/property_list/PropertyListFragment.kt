package com.openclassrooms.realestatemanager.presentation.ui.property_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyListBinding
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property_list.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class PropertyListFragment : Fragment(R.layout.fragment_property_list) {

    private var columnCount = 1
    private var _binding: FragmentPropertyListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPropertyListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = PropertyAdapter(PlaceholderContent.ITEMS)
        binding.list.adapter = adapter

        // Set the adapter
//        binding.list.apply {
//            layoutManager = when {
//                columnCount <= 1 -> LinearLayoutManager(context)
//                else -> GridLayoutManager(context, columnCount)
//            }
//            adapter = PropertyAdapter(PlaceholderContent.ITEMS)
//        }

        //openDetails(R.id.navigation_property_detail)
        return binding.root
    }

    fun openDetails(itemId: Int) {
        // Assume the NavHostFragment is added with the +id/detail_container.
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.detail_container
        ) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(
            // Assume the itemId is the android:id of a destination in the graph.
            itemId,
            null,
            NavOptions.Builder()
                // Pop all destinations off the back stack.
                .setPopUpTo(navController.graph.startDestination, true)
                .apply {
                    // If we're already open and the detail pane is visible,
                    // crossfade between the destinations.
                    if (binding.slidingPaneLayout.isOpen) {
                        setEnterAnim(R.animator.nav_default_enter_anim)
                        setExitAnim(R.animator.nav_default_exit_anim)
                    }
                }
                .build()
        )
        binding.slidingPaneLayout.open()
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
}
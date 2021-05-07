package com.openclassrooms.realestatemanager.presentation.ui.property_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PropertyListBinding
import com.openclassrooms.realestatemanager.presentation.ui.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property_list.placeholder.PlaceholderContent
import timber.log.Timber

/**
 * A fragment representing a list of Items.
 */
class PropertyListFragment : Fragment(R.layout.property_list),
    PropertyAdapter.PropertyClickListener {

    private var columnCount = 1
    private var _binding: PropertyListBinding? = null
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
        _binding = PropertyListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = PropertyAdapter(PlaceholderContent.ITEMS)
        binding.propertyList.adapter = adapter

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

    override fun onPropertyClick(view: View, property: PlaceholderContent.PlaceholderItem) {
        navigateToProperty(property, view)
        Timber.tag("click ").d("It's ok ")
    }

    private fun navigateToProperty(property: PlaceholderContent.PlaceholderItem, it: View?) {
        val directions =
            PropertyListFragmentDirections.actionPropertyListFragmentToPropertyDetailFragment(
                property.id
            )
        it?.findNavController()?.navigate(directions)
    }
}
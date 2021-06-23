package com.openclassrooms.realestatemanager.presentation.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragment
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListFragment
import com.openclassrooms.realestatemanager.presentation.ui.property_map.MapFragment
import javax.inject.Inject

class MainFragmentFactory
@Inject
constructor(
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            PropertyListFragment::class.java.name -> {
                val fragment = PropertyListFragment()
                fragment
            }
            MapFragment::class.java.name -> {
                val fragment = MapFragment()
                fragment
            }
            PropertyDetailFragment::class.java.name -> {
                val fragment = PropertyDetailFragment()
                fragment
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}

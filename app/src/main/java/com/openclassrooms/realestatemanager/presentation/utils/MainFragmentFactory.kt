package com.openclassrooms.realestatemanager.presentation.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListFragment
import javax.inject.Inject

class MainFragmentFactory
@Inject
constructor(
    private var properties: List<Property>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            PropertyListFragment::class.java.name -> {
                val fragment = PropertyListFragment(properties)
                fragment
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
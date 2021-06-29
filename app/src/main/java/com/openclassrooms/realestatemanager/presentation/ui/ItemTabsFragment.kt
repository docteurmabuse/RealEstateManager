package com.openclassrooms.realestatemanager.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.ui.adapters.TabsPagerAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListFragment
import com.openclassrooms.realestatemanager.presentation.ui.property_map.MapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemTabsFragment : Fragment() {

    private lateinit var tabsPagerAdapter: TabsPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(
            "key",
            true
        )
        val fragmentList = arrayListOf(
            PropertyListFragment(),
            MapFragment()
        )

        tabsPagerAdapter = TabsPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tabsPagerAdapter
        viewPager.isUserInputEnabled = false

        tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = " List"
                    tab.setIcon(R.drawable.ic_view_list_24dp)
                }

                1 -> {
                    tab.text = "Map"
                    tab.setIcon(R.drawable.ic_map_24dp)
                }
            }
        }.attach()

    }
}

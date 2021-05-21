package com.openclassrooms.realestatemanager.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.ui.adapters.TabsPagerAdapter
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListFragment
import com.openclassrooms.realestatemanager.presentation.ui.property_map.MapFragment

private const val ARG_OBJECT = "object"

/**
 * A simple [Fragment] subclass.
 * Use the [ItemTabsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemTabsFragment : Fragment() {

    private lateinit var tabsPagerAdapter: TabsPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fragmentList = arrayListOf<Fragment>(
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
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }
}
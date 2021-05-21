package com.openclassrooms.realestatemanager.presentation.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragment
import com.openclassrooms.realestatemanager.presentation.ui.MasterHostFragment

class TabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = ItemTabsFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return MasterHostFragment.newInstance(getReadableTabPosition(position))
    }

    private fun getReadableTabPosition(position: Int): Int {
        return position + 1
    }
}

private const val ARG_OBJECT = "object"

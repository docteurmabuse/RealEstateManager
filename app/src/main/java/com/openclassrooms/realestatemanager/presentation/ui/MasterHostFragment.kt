package com.openclassrooms.realestatemanager.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.openclassrooms.realestatemanager.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MasterHostFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.content_main, container, false)
        val master =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment?
        if (master != null) {
            val navController = master.navController
            val navInflater = navController.navInflater
            val graph = navInflater.inflate(R.navigation.mobile_navigation)

            master.navController.setGraph(graph, arguments)
        }

        return rootView
    }


    companion object {
        val TAB_NUMBER = "tab_number"

        fun newInstance(tabNumber: Int): MasterHostFragment {
            val fragment = MasterHostFragment()
            val bundle = Bundle()
            bundle.putInt(TAB_NUMBER, tabNumber)
            fragment.arguments = bundle

            return fragment
        }
    }
}
package com.openclassrooms.realestatemanager.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.BaseActivity
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import com.openclassrooms.realestatemanager.presentation.ui.addProperty.AddPropertyViewModel
import com.openclassrooms.realestatemanager.presentation.utils.MainFragmentFactory
import com.openclassrooms.realestatemanager.presentation.utils.MainNavHostFragment
import com.openclassrooms.realestatemanager.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity constructor(
    private var properties: List<Property>? = null
) : BaseActivity() {
    @Inject
    lateinit var fragmentFactory: MainFragmentFactory
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val addPropertyViewModel: AddPropertyViewModel by viewModels()
    private var isAddAgentView = false
    private var isAddPropertyView = false

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as MainNavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navController, appBarConfiguration)
        viewModel.fetchProperties()
        setObserver()
        setAddPropertyFabListener()
        setAddAgentFabListener()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.onQueryTextChanged {
            // update search query
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBundle("nav_state", navHostFragment.findNavController().saveState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        navHostFragment.findNavController().restoreState(savedInstanceState.getBundle("nav_state"))
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                true
            }
            R.id.action_sort_by_date_on_market -> {
                true
            }

            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        //Execute your code here
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

        val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
        navHostFragment.navigate(R.id.mainActivity)
    }


    private fun setAddAgentFabListener() {
        val expandableFab = binding.expandableFabPortrait

        binding.fabAddAgent.setOnClickListener {
            val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
            isAddAgentView = true
            isAddPropertyView = false

            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddAgentFragment(
                isAddAgentView
            )
            if (isAddAgentView) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.expandableFabLayout.removeAllViews()

            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.expandableFabLayout.removeAllViews()
            }

            navHostFragment.navigate(action)
        }
    }


    private fun setAddPropertyFabListener() {
        binding.fabAddProperty.setOnClickListener {
            val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
            val newPropertyId = UUID.randomUUID().toString()
            val action =
                ItemTabsFragmentDirections.actionItemTabsFragment2ToAddPropertyFragment(
                    newPropertyId, false, null
                )
            binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

            isAddPropertyView = true
            isAddAgentView = false
            binding.expandableFabLayout.removeAllViews()

            navHostFragment.navigate(action)
            Timber.tag("PROPERTY_ID").d("PROPERTY_ID: $newPropertyId")
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            val value = viewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { properties ->
                            renderList(properties)
                        }
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
        properties = list
        Timber.tag("MAP").d("MAP_PROPERTIES: ${properties!!.size}")
    }

}

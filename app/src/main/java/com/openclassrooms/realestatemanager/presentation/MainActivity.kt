package com.openclassrooms.realestatemanager.presentation

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.BaseActivity
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.addProperty.AddPropertyViewModel
import com.openclassrooms.realestatemanager.presentation.ui.property_list.PropertyListViewModel
import com.openclassrooms.realestatemanager.presentation.utils.MainFragmentFactory
import com.openclassrooms.realestatemanager.presentation.utils.MainNavHostFragment
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
    private val viewModel: PropertyListViewModel by viewModels()
    private val addPropertyViewModel: AddPropertyViewModel by viewModels()
    private var isAddAgentView = false
    private var isAddPropertyView = false

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as MainNavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    // creating variable that handles Animations loading
    // and initializing it with animation files that we have created
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    //used to check if fab menu are opened or closed
    private var closed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setObserver()
        setAddPropertyFabListener()
        setAddAgentFabListener()
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


    private fun showImages() {
        viewModel.fetchProperties()
    }

    private fun setAddAgentFabListener() {
        binding.fabAddAgent.setOnClickListener {
            val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
            isAddAgentView = true
            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddAgentFragment(
                isAddAgentView
            )
            navHostFragment.navigate(action)
        }
    }

    private fun setAddPropertyFabListener() {
        binding.fabAddProperty.setOnClickListener {
            val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
            val newPropertyId = UUID.randomUUID().toString()
            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddPropertyFragment(
                newPropertyId, false, null
            )
            isAddPropertyView = true
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

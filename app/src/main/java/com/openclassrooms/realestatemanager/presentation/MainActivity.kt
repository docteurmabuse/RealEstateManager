package com.openclassrooms.realestatemanager.presentation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.nambimobile.widgets.efab.ExpandableFabLayout
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import com.openclassrooms.realestatemanager.presentation.ui.agents.AgentsViewModel
import com.openclassrooms.realestatemanager.presentation.utils.MainFragmentFactory
import com.openclassrooms.realestatemanager.presentation.utils.MainNavHostFragment
import com.openclassrooms.realestatemanager.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity constructor(
    private var properties: List<Property>? = null
) : AppCompatActivity(), Toolbar.OnMenuItemClickListener,
    NavController.OnDestinationChangedListener {
    @Inject
    lateinit var fragmentFactory: MainFragmentFactory
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val agentViewModel: AgentsViewModel by viewModels()

    private var isAddAgentView = false
    private var isAddPropertyView = false
    private var agentList: List<Agent>? = arrayListOf()

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as MainNavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }
    private var isFabOpen = false

    val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navController, appBarConfiguration)
        viewModel.fetchProperties()
        setObserver()
        setupBottomNavigationAndFab()
        setAddPropertyFabListener()
        setAddAgentFabListener()
    }

    private fun setupBottomNavigationAndFab() {
        // Wrap binding.run to ensure ContentViewBindingDelegate is calling this Activity's
        // setContentView before accessing views
        binding.run {
            navController.addOnDestinationChangedListener(
                this@MainActivity
            )
        }

        // Set up the BottomAppBar menu
        binding.bottomAppBar.apply {
            setNavigationOnClickListener {
            }
            setOnMenuItemClickListener(this@MainActivity)
        }

        // Set a custom animation for showing and hiding the FAB
        /*binding..apply {
            setShowMotionSpecResource(R.animator.fab_show)
            setHideMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                navigateToCompose()
            }
        }*/
    }

    private fun setBottomAppBarForHome(@MenuRes menuRes: Int) {
        binding.run {
            // expandableFabLayout.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.replaceMenu(menuRes)
            expandableFabLayout.contentDescription = getString(R.string.hello_blank_fragment)
            expandableFabLayout.visibility = View.VISIBLE
            bottomAppBar.performShow()
            expandableFabLayout.isShown
        }
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_filter_properties -> {
                navController.navigate(R.id.action_itemTabsFragment2_to_propertySearchDialogFragment)
                Timber.d("FILTER: filter ok")
            }
        }
        return true
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.itemTabsFragment2 -> {
                setBottomAppBarForHome(getBottomAppBarMenuDestination(destination))
            }
            R.id.propertySearchDialogFragment -> {
                setBottomAppBarForHome(getBottomAppBarMenuDestination(destination))
                hideBottomAppBar()
            }
        }
    }

    private fun getBottomAppBarMenuDestination(destination: NavDestination? = null): Int {
        val dest = destination
            ?: findNavController(R.id.nav_host_fragment_activity_main).currentDestination
        return when (dest?.id) {
            R.id.itemTabsFragment2 -> R.menu.menu_fragment_properties
            R.id.propertySearchDialogFragment -> R.menu.edit_menu

            else -> R.menu.bottom_nav_menu
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val searchItem = binding.bottomAppBar.menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
            Timber.d("SEARCH: ${it}")
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun hideBottomAppBar() {
        binding.run {
            bottomAppBar.performHide()
            // Get a handle on the animator that hides the bottom app bar so we can wait to hide
            // the fab and bottom app bar until after it's exit animation finishes.
            bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    // Hide the BottomAppBar to avoid it showing above the keyboard
                    // when composing a new email.
                    bottomAppBar.visibility = View.GONE
                    expandableFabLayout.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_name ->
                Timber.d("SEARCH:CLICKED")


            R.id.action_sort_by_date_on_market -> {

            }

            R.id.action_search ->
                Timber.d("SEARCH:CLICKED")


            R.id.action_filter_properties ->
                Timber.d("FILTER:CLICKED")

            android.R.id.home -> {
                onBackPressed()
            }

        }
        return true
    }

    override fun onBackPressed() {
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        navController.navigate(R.id.mainActivity)
    }


    private fun setAddAgentFabListener() {

        binding.fabAddAgent.setOnClickListener {
            isAddAgentView = true
            isAddPropertyView = false

            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddAgentFragment()
            if (isAddAgentView) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.expandableFabLayout.visibility = View.GONE

            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.expandableFabLayout.visibility = View.GONE
            }
            navController.navigate(action)
        }
    }


    private fun setAddPropertyFabListener() {
        binding.fabAddProperty.setOnClickListener {
            isAddAgentView = false
            isAddPropertyView = true

            val action =
                ItemTabsFragmentDirections.actionItemTabsFragment2ToAddPropertyFragment(null)
            if (isAddPropertyView) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                findViewById<ExpandableFabLayout>(R.id.expandable_fab_layout).visibility = View.GONE

            } else {
                findViewById<ExpandableFabLayout>(R.id.expandable_fab_layout).visibility = View.GONE
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.expandableFabLayout.visibility = View.GONE
            }
            navController.navigate(action)
        }
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
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

        lifecycleScope.launchWhenStarted {
            val value = agentViewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { agents -> renderAgentList(agents) }
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

    private fun renderAgentList(agents: List<Agent>) {
        agentList = agents
        binding.fabAddProperty.fabOptionEnabled = agents.isNotEmpty()
    }

    private fun renderList(list: List<Property>) {
        properties = list
        Timber.tag("MAP").d("MAP_PROPERTIES: ${properties!!.size}")
    }


}

package com.openclassrooms.realestatemanager.presentation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.ItemTabsFragmentDirections
import com.openclassrooms.realestatemanager.presentation.ui.MainViewModel
import com.openclassrooms.realestatemanager.presentation.ui.SortOrder
import com.openclassrooms.realestatemanager.presentation.ui.agents.AgentsViewModel
import com.openclassrooms.realestatemanager.presentation.utils.MainFragmentFactory
import com.openclassrooms.realestatemanager.presentation.utils.MainNavHostFragment
import com.openclassrooms.realestatemanager.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener,
    NavController.OnDestinationChangedListener {
    @Inject
    lateinit var fragmentFactory: MainFragmentFactory
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val agentViewModel: AgentsViewModel by viewModels()
    private var properties: List<Property> = arrayListOf()
    private var isAddAgentView = false
    private var isAddPropertyView = false
    private var agentList: List<Agent>? = arrayListOf()
    private var isEuroCurrency = false
    var isOpen = false
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as MainNavHostFragment
    }

    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }
    private var menu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setFab()
        setupActionBarWithNavController(navController, appBarConfiguration)
        viewModel.fetchProperties()
        setObserver()
        setupBottomNavigationAndFab()
        setAddPropertyFabListener()
        setAddAgentFabListener()
    }

    private fun setFab() {
        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fabOpenRotate = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)
        val fabCloseRotate = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)
        binding.addFab.setOnClickListener {
            isOpen = if (isOpen) {
                binding.addAgentFab.startAnimation(fabClose)
                binding.addPropertyFab.startAnimation(fabClose)
                binding.addFab.startAnimation(fabOpenRotate)
                false
            } else {
                binding.addAgentFab.startAnimation(fabOpen)
                binding.addPropertyFab.startAnimation(fabOpen)
                binding.addFab.startAnimation(fabCloseRotate)
                binding.addAgentFab.isClickable
                binding.addPropertyFab.isClickable
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
            binding.addAgentFab.visibility = View.INVISIBLE
            binding.addPropertyFab.visibility = View.INVISIBLE
            // expandableFabPortrait.efabIcon =
            //    AppCompatResources.getDrawable(applicationContext, R.drawable.ic_add_24dp)

            bottomAppBar.performShow()
        }
    }

    private fun setBottomAppBarForAddEditPropertyFragment(@MenuRes menuRes: Int) {
        binding.run {
            // expandableFabLayout.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.replaceMenu(menuRes)
            binding.addAgentFab.visibility = View.INVISIBLE
            binding.addPropertyFab.visibility = View.INVISIBLE
            //   expandableFabPortrait.efabIcon =
            binding.addFab.setImageResource(R.drawable.ic_edit_24dp)
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            // bottomAppBar.performShow()
        }
    }

    private fun setBottomAppBarForAddEditAgentFragment(@MenuRes menuRes: Int) {
        binding.run {
            // expandableFabLayout.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.replaceMenu(menuRes)
            binding.addAgentFab.visibility = View.INVISIBLE
            binding.addPropertyFab.visibility = View.INVISIBLE
            //   expandableFabPortrait.efabIcon =
            binding.addFab.setImageResource(R.drawable.ic_edit_24dp)
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            // bottomAppBar.performShow()
        }
    }

    private fun setBottomAppBarForDetail(@MenuRes menuRes: Int) {
        binding.run {
            // expandableFabLayout.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomAppBar.visibility = View.VISIBLE
            bottomAppBar.replaceMenu(menuRes)
            binding.addAgentFab.visibility = View.INVISIBLE
            binding.addPropertyFab.visibility = View.INVISIBLE
            //   expandableFabPortrait.efabIcon =
            binding.addFab.setImageResource(R.drawable.ic_edit_24dp)
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            // bottomAppBar.performShow()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_currency -> {
                if (!isEuroCurrency) {
                    viewModel.toggleCurrency()
                    item.setIcon(R.drawable.ic_euro_symbol_24dp)
                } else {
                    viewModel.toggleCurrency()
                    item.setIcon(R.drawable.ic_attach_dollars_24dp)
                }
            }
            R.id.action_loan -> navController.navigate(R.id.action_itemTabsFragment2_to_loanFragment)
            R.id.action_filter_properties -> {
                navController.navigate(R.id.action_itemTabsFragment2_to_propertySearchDialogFragment)
                Timber.d("FILTER: filter ok")
            }
            R.id.action_sort_by_price_asc -> {
                viewModel.sortOrder.value = SortOrder.BY_PRICE_ASC
                viewModel.filterData()
                Timber.d("FILTER: price ASC ok")
            }
            R.id.action_sort_by_price_dsc -> {
                viewModel.sortOrder.value = SortOrder.BY_PRICE_DESC
                viewModel.filterData()
                Timber.d("FILTER: price ok")
            }
            R.id.action_sort_by_date_on_market_asc -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE_ASC
                viewModel.filterData()
            }
            R.id.action_sort_by_date_on_market_dsc -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE_DESC
                viewModel.filterData()
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
            R.id.propertyDetailFragment -> {
                setBottomAppBarForDetail(getBottomAppBarMenuDestination(destination))
                hideBottomAppBar()
            }
            R.id.propertySearchDialogFragment -> {
                setBottomAppBarForHome(getBottomAppBarMenuDestination(destination))
                hideBottomAppBar()
            }
            R.id.addEditAgentFragment ->
                setBottomAppBarForAddEditAgentFragment(getBottomAppBarMenuDestination(destination))

            R.id.addEditPropertyFragment ->
                setBottomAppBarForAddEditPropertyFragment(getBottomAppBarMenuDestination(destination))
        }


    }


    private fun getBottomAppBarMenuDestination(destination: NavDestination? = null): Int {
        val dest = destination
            ?: findNavController(R.id.nav_host_fragment_activity_main).currentDestination
        return when (dest?.id) {
            R.id.itemTabsFragment2 -> R.menu.menu_fragment_properties
            //     R.id.propertyDetailFragment -> R.menu.edit_menu
            else -> R.menu.menu_fragment_properties
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        if (isEuroCurrency)
            binding.bottomAppBar.menu?.findItem(R.id.action_currency)
                ?.setIcon(R.drawable.ic_euro_symbol_24dp)
        else
            binding.bottomAppBar.menu?.findItem(R.id.action_currency)
                ?.setIcon(R.drawable.ic_attach_dollars_24dp)

        val searchItem = binding.bottomAppBar.menu?.findItem(R.id.action_search)
        (searchItem?.actionView as? SearchView)?.onQueryTextChanged {
            viewModel.searchQuery.value = it
            viewModel.filterData()
            Timber.d("SEARCH: $it")
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
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    @ExperimentalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navLoan -> {
                Timber.d("LOAN")

            }
            R.id.action_search ->
                Timber.d("SEARCH:CLICKED")

            R.id.action_filter_properties ->
                Timber.d("FILTER:CLICKED")

            android.R.id.home -> {
                onBackPressed()
                this@MainActivity.onBackPressedDispatcher.addCallback(this) {
                    findNavController(R.id.nav_host_fragment_activity_main).popBackStack()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        navController.popBackStack()
        if (isEuroCurrency)
            binding.bottomAppBar.menu?.findItem(R.id.action_currency)
                ?.setIcon(R.drawable.ic_euro_symbol_24dp)
        else
            binding.bottomAppBar.menu?.findItem(R.id.action_currency)
                ?.setIcon(R.drawable.ic_attach_dollars_24dp)
    }

    private fun setAddAgentFabListener() {
        binding.addAgentFab.setOnClickListener {
            isAddAgentView = true
            isAddPropertyView = false

            val action = ItemTabsFragmentDirections.actionItemTabsFragment2ToAddAgentFragment()
            if (isAddAgentView) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            }
            navController.navigate(action)

        }
    }

    private fun setAddPropertyFabListener() {
        binding.addPropertyFab.setOnClickListener {
            isAddAgentView = false
            isAddPropertyView = true

            val action =
                ItemTabsFragmentDirections.actionItemTabsFragment2ToAddPropertyFragment(null)
            if (isAddPropertyView) {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            }
            navController.navigate(action)
        }
    }

    private fun setObserver() {
        lifecycleScope.launchWhenStarted {
            val value = agentViewModel.state
            value.collect {
                when (it.status) {
                    DataState.Status.SUCCESS -> {
                        it.data?.let { agents -> renderAgentList(agents) }
                        displayLoading(false)
                    }
                    DataState.Status.LOADING -> {
                        displayLoading(false)
                    }
                    DataState.Status.ERROR -> {
                        displayLoading(false)
                        displayError(it.message)
                        Timber.d("LIST_OBSERVER: ${it.message}")
                    }
                }
            }
        }
        viewModel.isEuroCurrency.observe(this@MainActivity) {
            this.isEuroCurrency = it
        }
    }

    private fun renderAgentList(agents: List<Agent>) {
        agentList = agents
        binding.addPropertyFab.isClickable = agents.isNotEmpty()
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}

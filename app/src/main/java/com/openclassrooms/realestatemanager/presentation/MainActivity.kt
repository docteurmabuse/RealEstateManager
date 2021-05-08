package com.openclassrooms.realestatemanager.presentation

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var isFabChecked = false

        binding.addPropertyFAB.setOnClickListener {
            isFabChecked = !isFabChecked

            Timber.tag("FabClick").d("It's ok FAB")


            var currentNavigation =
                findNavController(R.id.nav_host_fragment_activity_main).currentDestination
            Timber.tag("FabClick").d("FAB: $isFabChecked")
            val navHostFragment = findNavController(R.id.nav_host_fragment_activity_main)
            val addPropertyFragment = navHostFragment.graph.findNode(R.id.addPropertyFragment)
            if (isFabChecked) {
                binding.addPropertyFAB.hide(object :
                    FloatingActionButton.OnVisibilityChangedListener() {
                    override fun onShown(fab: FloatingActionButton?) {
                        super.onShown(fab)
                    }

                    override fun onHidden(fab: FloatingActionButton?) {
                        super.onHidden(fab)
                        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                        binding.bottomAppBar.replaceMenu(R.menu.edit_menu)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab?.setImageDrawable(getDrawable(R.drawable.ic_check_24dp))
                        } else {
                            fab?.setImageDrawable(resources.getDrawable(R.drawable.ic_check_24dp))
                        }

                        fab?.show()
                    }
                })

                binding.bottomAppBar.navigationIcon = null
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.addPropertyFragment)
            } else {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.propertyListFragment)
                binding.addPropertyFAB.hide(object :
                    FloatingActionButton.OnVisibilityChangedListener() {
                    override fun onShown(fab: FloatingActionButton?) {
                        super.onShown(fab)
                    }

                    override fun onHidden(fab: FloatingActionButton?) {
                        super.onHidden(fab)
                        binding.bottomAppBar.fabAlignmentMode =
                            BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        binding.bottomAppBar.replaceMenu(R.menu.bottom_app_bar)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab?.setImageDrawable(getDrawable(R.drawable.ic_add_24dp))
                        } else {
                            fab?.setImageDrawable(resources.getDrawable(R.drawable.ic_add_24dp))
                        }

                        fab?.show()
                    }
                })

                binding.bottomAppBar.navigationIcon = null
            }

        }
        setContentView(binding.root)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
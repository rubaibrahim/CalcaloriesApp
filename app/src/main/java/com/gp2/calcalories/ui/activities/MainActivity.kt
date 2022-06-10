package com.gp2.calcalories.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.App
import com.gp2.calcalories.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController

//        navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each  menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.planFragment,
            R.id.progressFragment,
            R.id.recipesFragment,
            R.id.notificationsFragment,
            R.id.profileFragment
        ))
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)

        // must add it to navController in onResume and remove it in  onPause
        listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.launchFragment -> {
                    setView(View.GONE, View.GONE, R.color.white, R.color.white)
                }
                R.id.loginFragment,R.id.registerFragment -> {
                    setView(View.GONE, View.GONE, R.color.white)
                }
                R.id.planFragment, R.id.progressFragment, R.id.recipesFragment, R.id.notificationsFragment, R.id.profileFragment -> {
                    setView(View.VISIBLE, View.VISIBLE, R.color.white)
                }
                else -> {
                    setView(View.VISIBLE, View.GONE, R.color.white)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        navController?.addOnDestinationChangedListener(listener)
        App.context = this
    }

    override fun onPause() {
        super.onPause()
        navController?.removeOnDestinationChangedListener(listener)
    }

    private fun setView(
        appBarVisibility: Int,
        navViewVisibility: Int,
        statusBarColor: Int,
        navigationBarColor: Int = R.color.black,
    ) {
        binding.appBar.visibility = appBarVisibility
        binding.navView.visibility = navViewVisibility
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        window.navigationBarColor = ContextCompat.getColor(this, navigationBarColor)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }
}
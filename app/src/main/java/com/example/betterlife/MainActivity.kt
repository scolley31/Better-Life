package com.example.betterlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.betterlife.data.CurrentFragmentType
import com.example.betterlife.databinding.ActivityMainBinding
import com.example.betterlife.ext.getVmFactory
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<MainViewMode>(){ getVmFactory() }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_main -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_other -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalOtherFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this


        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        setupBottomNav()
        setupNavController()
    }

    private fun setupBottomNav() {

        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView


    }

    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.otherFragment -> CurrentFragmentType.OTHER

                else -> viewModel.currentFragmentType.value
            }
        }
    }
}
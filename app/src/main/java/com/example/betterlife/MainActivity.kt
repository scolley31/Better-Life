package com.example.betterlife

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.betterlife.data.CurrentFragmentType
import com.example.betterlife.databinding.ActivityMainBinding
import com.example.betterlife.ext.getVmFactory
import com.example.betterlife.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : BaseActivity()  {

    val viewModel by viewModels<MainViewMode>(){ getVmFactory() }

    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_main -> {

                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid
                ))
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
        binding.viewModel = viewModel

        viewModel.currentFragmentType.observe(this, Observer {
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Logger.i("[${viewModel.currentFragmentType.value}]")
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        })

        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        setupToolbar()
        setupBottomNav()
        setupNavController()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        viewModel.currentFragmentType.observe(this) {
            Log.d("test", "currentFragmentType = ${viewModel.currentFragmentType.value}")
        }

        return super.onCreateView(name, context, attrs)
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
                R.id.timerFragment -> CurrentFragmentType.TIMER
                R.id.loginFragment -> CurrentFragmentType.LOGIN

                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupToolbar() {

        binding.toolbar.setPadding(0, statusBarHeight, 0, 0)

        launch {

            val dpi = resources.displayMetrics.densityDpi.toFloat()
            val dpiMultiple = dpi / DisplayMetrics.DENSITY_DEFAULT

            val cutoutHeight = getCutoutHeight()

            Logger.i("====== ${Build.MODEL} ======")
            Logger.i("$dpi dpi (${dpiMultiple}x)")
            Logger.i("statusBarHeight: ${statusBarHeight}px/${statusBarHeight / dpiMultiple}dp")

            when {
                cutoutHeight > 0 -> {
                    Logger.i("cutoutHeight: ${cutoutHeight}px/${cutoutHeight / dpiMultiple}dp")

                    val oriStatusBarHeight = resources.getDimensionPixelSize(R.dimen.height_status_bar_origin)

                    binding.toolbar.setPadding(0, oriStatusBarHeight, 0, 0)
                    val layoutParams = Toolbar.LayoutParams(
                            Toolbar.LayoutParams.WRAP_CONTENT,
                            Toolbar.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.gravity = Gravity.CENTER

                    when (Build.MODEL) {
                        "Pixel 5" -> {
                            Logger.i("Build.MODEL is ${Build.MODEL}")
                        }
                        else -> { layoutParams.topMargin = statusBarHeight - oriStatusBarHeight }
                    }
                    binding.textToolbarTitle.layoutParams = layoutParams
                }
            }
            Logger.i("====== ${Build.MODEL} ======")
        }
    }
}
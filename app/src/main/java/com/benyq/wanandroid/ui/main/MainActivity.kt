package com.benyq.wanandroid.ui.main

import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseActivity
import com.benyq.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()
//    private val navController: NavController get() = findNavController(R.id.nav_host_fragment)
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun onActivityCreated() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("benyq", "onDestinationChanged: $destination, $arguments")
        }


    }
}
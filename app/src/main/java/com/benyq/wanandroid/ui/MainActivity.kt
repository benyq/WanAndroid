package com.benyq.wanandroid.ui

import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.extensions.visibleOrGone
import com.benyq.wanandroid.base.BaseActivity
import com.benyq.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<ShareViewModel>()

    private val bottomNavigationItems = listOf(
        R.id.home_fragment,
        R.id.project_fragment,
        R.id.mine_fragment,
        R.id.category_fragment
    )

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun onActivityCreated() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibleOrGone(showBottomNavigation(destination.id))
        }

        viewModel.queryUserInfo()
    }

    private fun showBottomNavigation(@IdRes id: Int): Boolean {
        return bottomNavigationItems.contains(id)
    }
}
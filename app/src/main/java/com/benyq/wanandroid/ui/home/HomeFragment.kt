package com.benyq.wanandroid.ui.home

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseFragment
import com.benyq.wanandroid.databinding.FragmentHomeBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun getViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {

    }


    override fun observe() {
    }
}
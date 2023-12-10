package com.benyq.wanandroid.ui.project

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.databinding.FragmentProjectBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class ProjectFragment: BaseFragment<FragmentProjectBinding>(R.layout.fragment_project) {
    override fun getViewBinding(view: View) = FragmentProjectBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {

    }

    override fun observe() {

    }
}
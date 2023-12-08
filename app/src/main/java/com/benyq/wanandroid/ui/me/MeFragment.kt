package com.benyq.wanandroid.ui.me

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseFragment
import com.benyq.wanandroid.databinding.FragmentMeBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class MeFragment: BaseFragment<FragmentMeBinding>(R.layout.fragment_me) {
    override fun getViewBinding(view: View) = FragmentMeBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {

    }

    override fun observe() {

    }
}
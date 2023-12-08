package com.benyq.wanandroid.ui.mine

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseFragment
import com.benyq.wanandroid.databinding.FragmentMineBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class MineFragment: BaseFragment<FragmentMineBinding>(R.layout.fragment_mine) {
    override fun getViewBinding(view: View) = FragmentMineBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {

    }

    override fun observe() {

    }
}
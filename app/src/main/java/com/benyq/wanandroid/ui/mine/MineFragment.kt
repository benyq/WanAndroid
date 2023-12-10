package com.benyq.wanandroid.ui.mine

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.statusBarColor
import com.benyq.wanandroid.databinding.FragmentMineBinding
import com.benyq.wanandroid.ui.ShareViewModel

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class MineFragment : BaseFragment<FragmentMineBinding>(R.layout.fragment_mine) {

    private val shareViewModel by viewModels<ShareViewModel>(ownerProducer = { requireActivity() })

    override fun getViewBinding(view: View) = FragmentMineBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        statusBarColor(Color.BLUE)
    }

    override fun observe() {

    }
}
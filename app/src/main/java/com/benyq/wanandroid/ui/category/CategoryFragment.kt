package com.benyq.wanandroid.ui.category

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseFragment
import com.benyq.wanandroid.databinding.FragmentCategoryBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class CategoryFragment: BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    override fun getViewBinding(view: View) = FragmentCategoryBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {

    }

    override fun observe() {

    }
}
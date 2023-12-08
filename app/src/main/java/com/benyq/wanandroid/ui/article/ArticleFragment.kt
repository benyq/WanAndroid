package com.benyq.wanandroid.ui.article

import android.os.Bundle
import android.view.View
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.ui.BaseFragment
import com.benyq.wanandroid.databinding.FragmentArticleBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class ArticleFragment: BaseFragment<FragmentArticleBinding>(R.layout.fragment_article) {
    override fun getViewBinding(view: View) = FragmentArticleBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
    }

    override fun observe() {
    }
}
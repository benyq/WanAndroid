package com.benyq.wanandroid.ui.category.articles

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.ui.BaseListFragment
import com.benyq.wanandroid.ui.DataState
import com.benyq.wanandroid.ui.article.ArticleListAdapter
import com.chad.library.adapter4.loadState.LoadState

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */
class CategoryArticlesFragment: BaseListFragment() {

    private val viewModel by viewModels<CategoryArticlesViewModel>(ownerProducer = { this }, factoryProducer = { CategoryArticlesViewModelFactory(cid) })
    private val args: CategoryArticlesFragmentArgs by navArgs()
    private var cid: Int = 0

    private val adapter by lazy { ArticleListAdapter {
        findNavController().navigate(R.id.action_category_articles_to_article, Bundle().apply {
            putString("url", it.link)
            putString("title", it.title)
        })
    } }

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(savedInstanceState)
        cid = args.cid
        binding.tvTitle.text = args.title
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun observe() {
        viewModel.articlesFlow.collectOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    if (it.data.isFirst) {
                        adapter.submitList(it.data.articles)
                    }else {
                        adapter.addAll(it.data.articles)
                    }
                    helper.trailingLoadState = LoadState.NotLoading(it.data.isEnd)
                    finishRefresh()
                }
                is DataState.Error -> {
                    finishRefresh()
                }
                is DataState.Loading -> {

                }
            }
        }
    }

    override fun refresh() {
        viewModel.refresh()
    }

    override fun loadMore() {
        viewModel.loadMore()
    }

    override fun provideAdapter() = adapter
}
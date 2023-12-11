package com.benyq.wanandroid.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.statusBarColor
import com.benyq.wanandroid.databinding.FragmentHomeBinding
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>(ownerProducer = { this })
    private val articleAdapter by lazy {
        ArticleAdapter {
            findNavController().navigate(R.id.article_fragment)
        }
    }
    private val helper = QuickAdapterHelper.Builder(articleAdapter)
        // 使用默认样式的尾部"加载更多"
        .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
            override fun onLoad() {
                viewModel.loadMore()
            }

            override fun onFailRetry() {
                viewModel.loadMore()
            }

            override fun isAllowLoading(): Boolean {
                // 是否允许触发“加载更多”，通常情况下，下拉刷新的时候不允许进行加载更多
                return !binding.swipeLayout.isRefreshing
            }
        }).build()

    override fun getViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        statusBarColor(Color.BLACK)
        binding.swipeLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.rvArticle.setViewTreeLifecycleOwner(viewLifecycleOwner)
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.adapter = helper.adapter
    }


    override fun observe() {
        viewModel.articleFlow
            .distinctUntilChanged()
            .collectOnLifecycle(viewLifecycleOwner) { (data, isFirst, isOver) ->
                if (isFirst) {
                    articleAdapter.submitList(data)
                }else {
                    articleAdapter.addAll(data)
                }
                helper.trailingLoadState = LoadState.NotLoading(isOver)
                binding.swipeLayout.isRefreshing = false
            }
        viewModel.refreshingState
            .collectOnLifecycle(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = it
        }
    }
}
package com.benyq.wanandroid.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.dp
import com.benyq.wanandroid.base.extensions.visibleOrGone
import com.benyq.wanandroid.databinding.FragmentSearchBinding
import com.benyq.wanandroid.ui.DataState
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.google.android.flexbox.FlexboxLayoutManager

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */
class SearchFragment: BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel by viewModels<SearchViewModel>()

    private val helper by lazy {
        QuickAdapterHelper.Builder(viewModel.articleAdapter)
            // 使用默认样式的尾部"加载更多"
            .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onLoad() {
                    viewModel.loadMore()
                }

                override fun onFailRetry() {
                    viewModel.loadMore()
                }

                override fun isAllowLoading(): Boolean {
                    return true
                }
            }).build()
    }


    override fun getViewBinding(view: View) = FragmentSearchBinding.bind(view)
    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        val divider = object: ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 5.dp
                outRect.right = 5.dp
            }
        }
        binding.rvHotKey.layoutManager = FlexboxLayoutManager(requireActivity())
        binding.rvHotKey.adapter = viewModel.hotKeyAdapter
        binding.rvHotKey.addItemDecoration(divider)

        binding.rvSearchHistory.layoutManager = FlexboxLayoutManager(requireActivity())
        binding.rvSearchHistory.addItemDecoration(divider)
        binding.rvSearchHistory.adapter = viewModel.searchHistoryAdapter

        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearchResult.adapter = helper.adapter

        binding.tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.etSearch.text.toString())
            }
            true
        }
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            binding.ivCancelDelete.visibleOrGone(text.isNullOrBlank().not())
            if (text.isNullOrEmpty()) {
                viewModel.exitSearchResult()
                viewModel.articleAdapter.submitList(emptyList())
            }

        }
        binding.ivCancelDelete.setOnClickListener {
            binding.etSearch.setText("")
        }
        binding.ivDelete.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    override fun observe() {
        viewModel.currentType.collectOnLifecycle(viewLifecycleOwner) {
            binding.llSearch.visibleOrGone(it == SearchViewModel.TYPE_HISTORY)
            binding.rvSearchResult.visibleOrGone(it == SearchViewModel.TYPE_RESULT)
        }
        viewModel.searchEventFlow.collectOnLifecycle(viewLifecycleOwner) {
            when(it) {
                is SearchEvent.Search -> {
                    binding.etSearch.setText(it.keyword)
                    viewModel.search(it.keyword)
                }
                is SearchEvent.toArticle -> {
                    findNavController().navigate(R.id.action_search_to_article, Bundle().apply {
                        putString("url", it.article.link)
                        putString("title", it.article.title)
                    })
                }
            }
        }
        viewModel.articlesFlow.collectOnLifecycle(viewLifecycleOwner) {
            when(it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    val state = it.data
                    if (state.isFirst) {
                        viewModel.articleAdapter.submitList(state.articles)
                    }else {
                        viewModel.articleAdapter.addAll(state.articles)
                    }
                    helper.trailingLoadState = LoadState.NotLoading(state.isEnd)
                }
                is DataState.Error -> {
                }
            }
        }
        viewModel.searchStateFlow.collectOnLifecycle(viewLifecycleOwner) {
            binding.ivDelete.alpha = if (it.isSearchHistoryEmpty) 0.4f else 1f
        }
    }
}
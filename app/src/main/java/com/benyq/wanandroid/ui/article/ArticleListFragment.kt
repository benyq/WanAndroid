package com.benyq.wanandroid.ui.article

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.databinding.FragmentArticleListBinding
import com.benyq.wanandroid.ui.home.ArticleAdapter
import com.benyq.wanandroid.ui.home.BannerArticleModel
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */
class ArticleListFragment: BaseFragment<FragmentArticleListBinding>(R.layout.fragment_article_list) {

    private val viewModel by viewModels<ArticleListViewModel>(ownerProducer = { this })
    private val args: ArticleListFragmentArgs by navArgs()
    private var cid: Int = 0
    private val articleAdapter by lazy { ArticleAdapter { article, _ ->
        findNavController().navigate(R.id.action_article_list_to_article, Bundle().apply {
            article?.let {
                putString("url", it.link)
                putString("title", it.title)
            }
        })
    }}

    private val helper = QuickAdapterHelper.Builder(articleAdapter)
        // 使用默认样式的尾部"加载更多"
        .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
            override fun onLoad() {
                viewModel.loadMore(cid)
            }

            override fun onFailRetry() {
                viewModel.loadMore(cid)
            }

            override fun isAllowLoading(): Boolean {
                // 是否允许触发“加载更多”，通常情况下，下拉刷新的时候不允许进行加载更多
                return !binding.swipeLayout.isRefreshing
            }
        }).build()


    override fun getViewBinding(view: View) = FragmentArticleListBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        cid = args.cid

        binding.rvArticle.adapter = helper.adapter
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())

        binding.tvTitle.text = args.title

        binding.swipeLayout.setOnRefreshListener {
            viewModel.refresh(cid)
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun observe() {
        viewModel.state.collectOnLifecycle(viewLifecycleOwner) {
            if (it.isFirst) {
                articleAdapter.submitList(it.articles.map { article-> BannerArticleModel(article, null) })
            }else {
                articleAdapter.addAll(it.articles.map { article-> BannerArticleModel(article, null) })
            }
            helper.trailingLoadState = LoadState.NotLoading(it.isEnd)
            binding.swipeLayout.isRefreshing = false
        }
    }

    override fun initData() {
        viewModel.refresh(cid)
    }
}
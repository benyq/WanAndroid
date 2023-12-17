package com.benyq.wanandroid.ui.project.articles

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.dp
import com.benyq.wanandroid.base.extensions.gone
import com.benyq.wanandroid.ui.BaseListFragment
import com.chad.library.adapter4.loadState.LoadState
import java.lang.Exception

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */
class ProjectArticlesFragment : BaseListFragment() {

    companion object {
        fun newInstance(cid: Int): ProjectArticlesFragment {
            return ProjectArticlesFragment().apply {
                val bundle = Bundle()
                bundle.putInt("cid", cid)
                bundle.putString("title", "")
                arguments = bundle
            }
        }
    }

    private val args: ProjectArticlesFragmentArgs by navArgs()
    private var cid: Int = 0

    private val viewModel by viewModels<ProjectArticlesViewModel>(
        factoryProducer = { ProjectArticlesViewModelFactory(cid) })

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        cid = args.cid
        super.onFragmentViewCreated(savedInstanceState)

        binding.tvTitle.text = args.title
        binding.clHead.gone()

        binding.recyclerView.setPadding(10.dp, 0, 10.dp, 0)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(ColorDrawable(Color.BLACK))
            })
        binding.recyclerView.addItemDecoration(object: ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.top = 10.dp
                }else {
                    outRect.top = 5.dp
                }
                outRect.bottom = 5.dp
            }
        })
        binding.swipeLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun refresh() {
        viewModel.refresh()
    }

    override fun loadMore() {
        viewModel.loadMore()
    }

    override fun provideAdapter() = viewModel.articleAdapter
    override fun isAllowLoading(): Boolean {
        return try {
            !binding.swipeLayout.isRefreshing
        }catch (e: Exception) {
            true
        }
    }

    override fun observe() {
        viewModel.state.collectOnLifecycle(viewLifecycleOwner) {
            if (it.isFirst) {
                viewModel.articleAdapter.submitList(it.articles)
            } else {
                viewModel.articleAdapter.addAll(it.articles)
            }
            helper.trailingLoadState = LoadState.NotLoading(it.isEnd)
            binding.swipeLayout.isRefreshing = false
        }
        viewModel.event.collectOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ProjectEvent.NavigateToArticle -> {
                    it.article?.let { article ->
                        findNavController().navigate(
                            R.id.action_project_to_article,
                            Bundle().apply {
                                putString("url", article.link)
                                putString("title", article.title)
                            })
                    }
                }
            }
        }
    }

}
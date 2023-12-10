package com.benyq.wanandroid.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.statusBarColor
import com.benyq.wanandroid.databinding.FragmentHomeBinding
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>(ownerProducer = { this })
    private val bannerAdapter by lazy { ImageBannerAdapter() }
    private val articleAdapter by lazy {
        ArticleAdapter {
            findNavController().navigate(R.id.article_fragment)
        }
    }

    override fun getViewBinding(view: View) = FragmentHomeBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        statusBarColor(Color.BLACK)
        binding.bannerView.setAdapter(bannerAdapter)
            .setPageTransformer(ScaleInTransformer(0.7f))
            .addBannerLifecycleObserver(viewLifecycleOwner)//添加生命周期观察者
            .setIndicator(CircleIndicator(requireActivity()))
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvArticle.adapter = articleAdapter
    }


    override fun observe() {
        viewModel.bannerStateFlow.collectOnLifecycle(viewLifecycleOwner) {
            if (bannerAdapter.itemCount == 0) {
                bannerAdapter.setDatas(it)
            }
        }
        viewModel.articleStateFlow
            .collectOnLifecycle(viewLifecycleOwner) {
                if (articleAdapter.itemCount != it.size) {
                    articleAdapter.submitList(it)
                }
            }
    }
}
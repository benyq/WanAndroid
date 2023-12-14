package com.benyq.wanandroid.ui.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.overScrollNever
import com.benyq.wanandroid.databinding.FragmentProjectBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class ProjectTreeFragment : BaseFragment<FragmentProjectBinding>(R.layout.fragment_project) {

    private val viewModel by viewModels<ProjectTreeViewModel>(ownerProducer = { this })
    private val vpAdapter by lazy { ProjectTreeAdapter(mutableListOf(), this) }
    override fun getViewBinding(view: View) = FragmentProjectBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        binding.ivSearch.setOnClickListener {

        }
        binding.tabs.tabCount
        binding.vp2.overScrollNever()
    }

    override fun observe() {
        viewModel.tree.collectOnLifecycle(viewLifecycleOwner) {
            if (it.isEmpty()) return@collectOnLifecycle
            binding.vp2.adapter = ProjectTreeAdapter(it, this)
            TabLayoutMediator(binding.tabs, binding.vp2) { tab, position ->
                tab.text = it[position].name
            }.attach()
        }
    }
}
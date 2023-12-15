package com.benyq.wanandroid.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.databinding.FragmentCategoryBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class CategoryFragment : BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    private val viewModel by viewModels<CategoryViewModel>()
    private val categoryTreeAdapter by lazy {
        CategoryTreeAdapter {
            val action = CategoryFragmentDirections.actionCategoryToArticles(it.id, it.name)
            findNavController().navigate(action)
        }
    }

    override fun getViewBinding(view: View) = FragmentCategoryBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        binding.rvCategoryTree.adapter = categoryTreeAdapter
        binding.rvCategoryTree.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun observe() {
        viewModel.categoryTreeFlow.collectOnLifecycle(viewLifecycleOwner) {
            categoryTreeAdapter.submitList(it)
        }
    }
}
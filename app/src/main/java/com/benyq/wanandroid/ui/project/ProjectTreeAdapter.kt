package com.benyq.wanandroid.ui.project

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.benyq.wanandroid.model.ProjectTreeModel
import com.benyq.wanandroid.ui.project.articles.ProjectArticlesFragment

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */
class ProjectTreeAdapter(
    private val data: List<ProjectTreeModel>,
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = data.size

    override fun createFragment(position: Int): Fragment {
        val item = data[position]
        return ProjectArticlesFragment.newInstance(item.id)
    }

}
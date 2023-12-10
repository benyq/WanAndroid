package com.benyq.wanandroid.ui.home

import android.content.Context
import android.view.ViewGroup
import com.benyq.wanandroid.R
import com.benyq.wanandroid.model.ArticleModel
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

class ArticleAdapter(private val action: (ArticleModel) -> Unit): BaseQuickAdapter<ArticleModel, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ArticleModel?) {
        item?.let {
            holder.setText(R.id.tv_author, it.author.ifEmpty { it.shareUser })
            holder.setText(R.id.tv_title, it.title)
            holder.setText(R.id.tv_datetime, it.niceDate)
            holder.itemView.setOnClickListener {
                action(item)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_home_article, parent)
    }
}
package com.benyq.wanandroid.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benyq.wanandroid.R
import com.benyq.wanandroid.databinding.ItemHomeArticleBinding
import com.benyq.wanandroid.model.ArticleModel
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */
class ArticleListAdapter(private val action: (ArticleModel)->Unit) : BaseQuickAdapter<ArticleModel, ArticleListAdapter.ArticleVH>() {

    class ArticleVH(val viewBinding: ItemHomeArticleBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: ArticleVH, position: Int, item: ArticleModel?) {
        item?.let {
            holder.viewBinding.tvAuthor.text = it.author.ifEmpty { it.shareUser }
            holder.viewBinding.tvTitle.text = it.title
            holder.viewBinding.tvTags.text = if (it.superChapterName.isNullOrEmpty()) it.chapterName else "${it.superChapterName}/${it.superChapterName}"
            holder.viewBinding.tvDatetime.text = it.niceDate
            holder.itemView.setOnClickListener {
                action(item)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): ArticleVH {
        return ArticleVH(ItemHomeArticleBinding.inflate(LayoutInflater.from(context), parent, false))
    }
}
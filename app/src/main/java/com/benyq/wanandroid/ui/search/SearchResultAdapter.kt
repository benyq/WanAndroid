package com.benyq.wanandroid.ui.search

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benyq.wanandroid.databinding.ItemHomeArticleBinding
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.ui.article.ArticleListAdapter
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

/**
 *
 * @author benyq
 * @date 12/15/2023
 *
 */
class SearchResultAdapter(private val action: (ArticleModel)->Unit): BaseQuickAdapter<ArticleModel, SearchResultAdapter.ArticleVH>() {
    class ArticleVH(val viewBinding: ItemHomeArticleBinding) : RecyclerView.ViewHolder(viewBinding.root)

    private var searchKeys = emptyList<String>()

    fun updateSearchKey(key:String){
        searchKeys = key.split(" ")
    }

    override fun onBindViewHolder(holder: ArticleVH, position: Int, item: ArticleModel?) {
        item?.let {
            holder.viewBinding.tvAuthor.text = it.author.ifEmpty { it.shareUser }
            val spannableString = SpannableString(it.title)
            searchKeys.forEach { key->
                if (item.title.contains(key)) {
                    val index = item.title.indexOf(key)
                    spannableString.setSpan(ForegroundColorSpan(Color.RED), index, index + key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            holder.viewBinding.tvTitle.text = spannableString
            holder.viewBinding.tvTags.text = "${it.superChapterName}/${it.chapterName}"
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
        return ArticleVH(
            ItemHomeArticleBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }
}
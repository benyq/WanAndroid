package com.benyq.wanandroid.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.benyq.wanandroid.R
import com.benyq.wanandroid.databinding.ItemBannerHolderBinding
import com.benyq.wanandroid.databinding.ItemHomeArticleBinding
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.model.BannerModel
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.zhpan.bannerview.BannerViewPager
import kotlin.math.log

class ArticleAdapter(private val action: (ArticleModel?, BannerModel?) -> Unit): BaseMultiItemAdapter<BannerArticleModel>() {
    class ArticleVH(val viewBinding: ItemHomeArticleBinding) : RecyclerView.ViewHolder(viewBinding.root)

    class BannerVH(viewBinding: ItemBannerHolderBinding) : RecyclerView.ViewHolder(viewBinding.root)


    init {
        addItemType(BANNER_TYPE, object: OnMultiItemAdapterListener<BannerArticleModel, BannerVH> {
            override fun onBind(holder: BannerVH, position: Int, item: BannerArticleModel?) {
                val bannerView: BannerViewPager<BannerModel> = holder.itemView.findViewById(R.id.banner_view)
                Log.d("ArticleAdapter", "onBind: life: ${recyclerView.findViewTreeLifecycleOwner()}")
                if (bannerView.adapter == null || bannerView.adapter.itemCount != item?.banners?.size) {
                    bannerView.adapter = ImageBannerAdapter {
                        action(null, it)
                    }
                    bannerView.create(item?.banners)
                }
                recyclerView.findViewTreeLifecycleOwner()?.let {
                    bannerView.registerLifecycleObserver(it.lifecycle)
                }
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): BannerVH {
                Log.d("ArticleAdapter", "onBind: onCreate: ")
                val viewBinding = ItemBannerHolderBinding.inflate(LayoutInflater.from(context), parent, false)
                return BannerVH(viewBinding)
            }

        }).addItemType(ARTICLE_TYPE, object: OnMultiItemAdapterListener<BannerArticleModel, ArticleVH>{
            @SuppressLint("SetTextI18n")
            override fun onBind(holder: ArticleVH, position: Int, item: BannerArticleModel?) {
                item?.articleModel?.let {
                    holder.viewBinding.tvAuthor.text = it.author.ifEmpty { it.shareUser }
                    holder.viewBinding.tvTitle.text = it.title
                    holder.viewBinding.tvTags.text = "${it.superChapterName}/${it.superChapterName}"
                    holder.viewBinding.tvDatetime.text = it.niceDate
                    holder.itemView.setOnClickListener {
                        action(item.articleModel, null)
                    }
                }
            }
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): ArticleVH {
                val viewBinding = ItemHomeArticleBinding.inflate(LayoutInflater.from(context), parent, false)
                return ArticleVH(viewBinding)
            }
        }).onItemViewType {position, list ->
            if (list[position].articleModel != null) {
                ARTICLE_TYPE
            } else {
                BANNER_TYPE
            }
        }
    }

    companion object {
        private const val BANNER_TYPE = 0
        private const val ARTICLE_TYPE = 1
    }
}
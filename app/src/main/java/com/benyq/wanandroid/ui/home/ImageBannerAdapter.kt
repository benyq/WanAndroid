package com.benyq.wanandroid.ui.home

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.extensions.px
import com.benyq.wanandroid.model.BannerModel
import com.youth.banner.adapter.BannerAdapter


class ImageBannerAdapter: BannerAdapter<BannerModel, ImageBannerAdapter.BannerViewHolder>(listOf()) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: BannerModel, position: Int, size: Int) {
        holder.imageView.load(data.imagePath)
    }

    class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

}
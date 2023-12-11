package com.benyq.wanandroid.ui.home

import android.widget.ImageView
import coil.load
import com.benyq.wanandroid.R
import com.benyq.wanandroid.model.BannerModel
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


class ImageBannerAdapter(private val action: () -> Unit): BaseBannerAdapter<BannerModel>() {

    override fun bindData(
        holder: BaseViewHolder<BannerModel>?,
        data: BannerModel?,
        position: Int,
        pageSize: Int,
    ) {
        data?.let {
            val imageView = holder?.findViewById<ImageView>(R.id.image)
            imageView?.load(it.imagePath)
            holder?.itemView?.setOnClickListener {
                action()
            }
        }
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_banner

}
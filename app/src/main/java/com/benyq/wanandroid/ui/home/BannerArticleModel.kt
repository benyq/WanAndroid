package com.benyq.wanandroid.ui.home

import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.model.BannerModel

/**
 *
 * @author benyq
 * @date 12/11/2023
 *
 */
data class BannerArticleModel(
    val articleModel: ArticleModel? = null,
    val banners: List<BannerModel>? = null
)

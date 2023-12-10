package com.benyq.wanandroid.base.network

import com.benyq.wanandroid.base.extensions.appCtx
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.model.BannerModel
import com.benyq.wanandroid.model.PageModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("banner/json")
    suspend fun banner(): ApiResponse<List<BannerModel>>

    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page: Int): ApiResponse<PageModel<ArticleModel>>
}


val apiService by lazy { RetrofitCreator().createRetrofit(appCtx) }

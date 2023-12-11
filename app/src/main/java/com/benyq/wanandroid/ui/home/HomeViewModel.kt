package com.benyq.wanandroid.ui.home

import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.ApiResponse
import com.benyq.wanandroid.base.network.apiService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class HomeViewModel : BaseViewModel() {

    //first list, second isFirstLoad, third isOver
    private val _articleFlow =
        MutableSharedFlow<Triple<List<BannerArticleModel>, Boolean, Boolean>>()
    val articleFlow: SharedFlow<Triple<List<BannerArticleModel>, Boolean, Boolean>> = _articleFlow
    private var pageIndex = 0

    private val _refreshingState = MutableSharedFlow<Boolean>()
    val refreshingState: SharedFlow<Boolean> = _refreshingState

    init {
        refresh()
    }

    fun refresh() {
        pageIndex = 0
        execute {
            val banner = async { apiService.banner() }
            val article = async { apiService.articles(pageIndex) }
            val bannerResponse = banner.await()
            val articleResponse = article.await()

            if (!bannerResponse.isSuccess) {
                throw Exception(bannerResponse.errorMsg)
            }
            if (!articleResponse.isSuccess) {
                throw Exception(articleResponse.errorMsg)
            }
            ApiResponse.success(listOf(BannerArticleModel(banners = bannerResponse.data))
                    + articleResponse.data!!.datas.map {
                BannerArticleModel(articleModel = it)
            })
        }.onSuccess { response ->
            _articleFlow.emit(Triple(response.data!!, true, false))
            pageIndex++
        }.onFinally {
            _refreshingState.emit(false)
        }
    }

    private fun getArticle(page: Int) {
        execute {
            apiService.articles(page)
        }.onSuccess { response ->
            if (response.isSuccess) {
                _articleFlow.emit(Triple(response.data!!.datas.map { article ->
                    BannerArticleModel(articleModel = article)
                }, false, response.data.over))
                pageIndex++
            }
        }
    }

    fun loadMore() {
        getArticle(pageIndex)
    }

}
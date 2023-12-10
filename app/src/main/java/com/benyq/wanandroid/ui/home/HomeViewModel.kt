package com.benyq.wanandroid.ui.home

import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.model.BannerModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: BaseViewModel() {

    private val _bannerStateFlow = MutableStateFlow<List<BannerModel>>(listOf())
    val bannerStateFlow: StateFlow<List<BannerModel>> = _bannerStateFlow

    private val _articleStateFlow = MutableStateFlow<List<ArticleModel>>(listOf())
    val articleStateFlow: StateFlow<List<ArticleModel>> = _articleStateFlow
    private var pageIndex = 0

    private val _loadMoreStateFlow = MutableStateFlow(false)
    val loadMoreStateFlow = _loadMoreStateFlow

    init {
        getBanner()
        getArticle(pageIndex)
    }

    private fun getBanner() {
        execute {
            apiService.banner()
        }.onSuccess {
            if (it.isSuccess) {
                _bannerStateFlow.emit(it.data!!)
            }
        }
    }

    private fun getArticle(page: Int) {
        execute {
            apiService.articles(page)
        }.onSuccess { response->
            if (response.isSuccess) {
                if (page == 0) {
                    _articleStateFlow.emit(response.data!!.datas)
                }else {
                    _articleStateFlow.update {
                        it + response.data!!.datas
                    }
                }
                _loadMoreStateFlow.emit(response.data!!.over)
                pageIndex++
            }
        }
    }

    fun loadMore() {
        getArticle(pageIndex)
    }

    fun refresh() {
        pageIndex = 0
        getArticle(pageIndex)
    }

}
package com.benyq.wanandroid.ui.category.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleListState
import com.benyq.wanandroid.ui.DataState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */

class CategoryArticlesViewModelFactory(private val cid: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.cast(CategoryArticlesViewModel(cid))!!
    }
}


class CategoryArticlesViewModel(private val cid: Int): BaseViewModel() {

    private val _articlesFlow = MutableSharedFlow<DataState<ArticleListState>>()
    val articlesFlow: SharedFlow<DataState<ArticleListState>> = _articlesFlow

    init {
        getCategoryArticles()
    }

    private var _pageIndex = 0

    private fun getCategoryArticles() {
        flowResponse {
            apiService.articles(_pageIndex, cid)
        }.onEach {
            val mapData = it.map { pageModel ->
                val isFirst = _pageIndex == 0
                _pageIndex++
                ArticleListState(isFirst, pageModel.over, pageModel.datas)
            }
            _articlesFlow.emit(mapData)
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        _pageIndex = 0
        getCategoryArticles()
    }

    fun loadMore() {
        getCategoryArticles()
    }
}
package com.benyq.wanandroid.ui.article

import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.ui.DataState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */

data class ArticleListState(
    val isFirst: Boolean = true,
    val isEnd: Boolean = false,
    val articles: List<ArticleModel>
)

class ArticleListViewModel: BaseViewModel() {
    private val _state = MutableSharedFlow<ArticleListState>()
    val state: SharedFlow<ArticleListState> = _state

    private var _pageIndex = 0

    private fun queryArticles(cid: Int) {
        flowResponse {
            apiService.articles(_pageIndex, cid)
        }.onEach {
            if (it is DataState.Success) {
                _state.emit(ArticleListState(
                    isFirst = _pageIndex == 0,
                    isEnd = it.data.over,
                    articles = it.data.datas
                ))
                _pageIndex++
            }
        }.launchIn(viewModelScope)
    }

    fun refresh(cid: Int) {
        _pageIndex = 0
        queryArticles(cid)
    }

    fun loadMore(cid: Int) {
        queryArticles(cid)
    }

}
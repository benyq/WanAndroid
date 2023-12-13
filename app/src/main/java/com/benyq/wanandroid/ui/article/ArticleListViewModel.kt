package com.benyq.wanandroid.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.ui.DataState
import com.benyq.wanandroid.ui.home.ArticleAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */

data class ArticleListState(
    val isFirst: Boolean = true,
    val isEnd: Boolean = false,
    val articles: List<ArticleModel>,
)

sealed class ArticleListEvent {
    data class NavigateToArticle(val article: ArticleModel?) : ArticleListEvent()
}

class ArticleListViewModelFactory(private val cid: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.cast(ArticleListViewModel(cid))!!
    }
}

class ArticleListViewModel(private val cid: Int) : BaseViewModel() {
    private val _state = MutableSharedFlow<ArticleListState>()
    val state: SharedFlow<ArticleListState> = _state

    private val _event = MutableSharedFlow<ArticleListEvent>()
    val event: SharedFlow<ArticleListEvent> = _event

    private var _pageIndex = 0

    val articleAdapter by lazy {
        ArticleAdapter { article, _ ->
            viewModelScope.launch {
                _event.emit(ArticleListEvent.NavigateToArticle(article))
            }
        }
    }

    init {
        refresh()
    }

    private fun queryArticles() {
        flowResponse {
            apiService.articles(_pageIndex, cid)
        }.onEach {
            if (it is DataState.Success) {
                _state.emit(
                    ArticleListState(
                        isFirst = _pageIndex == 0,
                        isEnd = it.data.over,
                        articles = it.data.datas
                    )
                )
                _pageIndex++
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        _pageIndex = 0
        queryArticles()
    }

    fun loadMore() {
        queryArticles()
    }

}
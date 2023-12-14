package com.benyq.wanandroid.ui.project.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleListState
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.ui.DataState
import com.benyq.wanandroid.ui.home.BannerArticleAdapter
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

sealed class ProjectEvent {
    data class NavigateToArticle(val article: ArticleModel?) : ProjectEvent()
}

class ProjectArticlesViewModelFactory(private val cid: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.cast(ProjectArticlesViewModel(cid))!!
    }
}

class ProjectArticlesViewModel(private val cid: Int) : BaseViewModel() {
    private val _state = MutableSharedFlow<ArticleListState>()
    val state: SharedFlow<ArticleListState> = _state

    private val _event = MutableSharedFlow<ProjectEvent>()
    val event: SharedFlow<ProjectEvent> = _event

    private var _pageIndex = 0

    val articleAdapter by lazy {
        ProjectArticlesAdapter { article->
            viewModelScope.launch {
                _event.emit(ProjectEvent.NavigateToArticle(article))
            }
        }
    }

    init {
        refresh()
    }

    private fun queryArticles() {
        flowResponse {
            apiService.projects(_pageIndex, cid)
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
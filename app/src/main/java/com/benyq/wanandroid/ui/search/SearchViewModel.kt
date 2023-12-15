package com.benyq.wanandroid.ui.search

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.extensions.appCtx
import com.benyq.wanandroid.base.extensions.dataStore
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ArticleListState
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.ui.DataState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */

sealed class SearchEvent {
    class Search(val keyword: String) : SearchEvent()
    class toArticle(val article: ArticleModel) : SearchEvent()
}

data class SearchState(
    val isSearchHistoryEmpty: Boolean = true,
)

class SearchViewModel : BaseViewModel() {
    companion object {
        const val TYPE_HISTORY = 0
        const val TYPE_RESULT = 1
    }

    private val _currentType = MutableStateFlow(TYPE_HISTORY)
    val currentType: StateFlow<Int> = _currentType

    private val _searchEventFlow = MutableSharedFlow<SearchEvent>()
    val searchEventFlow: SharedFlow<SearchEvent> = _searchEventFlow

    private val _searchStateFlow = MutableStateFlow(SearchState())
    val searchStateFlow: SharedFlow<SearchState> = _searchStateFlow

    val hotKeyAdapter by lazy {
        HotKeyAdapter {
            viewModelScope.launch {
                _searchEventFlow.emit(SearchEvent.Search(it))
            }
        }
    }
    val searchHistoryAdapter by lazy {
        SearchHistoryAdapter {
            viewModelScope.launch {
                _searchEventFlow.emit(SearchEvent.Search(it))
            }
        }
    }
    val articleAdapter by lazy {
        SearchResultAdapter {
            viewModelScope.launch {
                _searchEventFlow.emit(SearchEvent.toArticle(it))
            }
        }
    }

    private var _searchKey = ""
    private var _pageIndex = 0

    private val _articlesFlow = MutableSharedFlow<DataState<ArticleListState>>()
    val articlesFlow: SharedFlow<DataState<ArticleListState>> = _articlesFlow

    init {
        getHotKey()
        getSearchHistory()
    }

    fun search(keyword: String) {
        _searchKey = keyword
        _pageIndex = 0
        _currentType.update {
            TYPE_RESULT
        }
        updateSearchHistory(keyword)
        articleAdapter.updateSearchKey(keyword)
        doSearch()
    }

    fun exitSearchResult() {
        _currentType.update {
            TYPE_HISTORY
        }
    }

    fun loadMore() {
        doSearch()
    }

    private fun doSearch() {
        execute {
            val response = apiService.search(_pageIndex, _searchKey)
            if (response.isSuccess) {
                val pageModel = response.data!!
                val newList = pageModel.datas.map { article->
                    val title = removeHtmlTag(article.title)
                    article.copy(title = title)
                }
                pageModel.copy(datas = newList)
            }else {
                throw Exception(response.errorMsg)
            }
        }.onStart {
            _articlesFlow.emit(DataState.Loading(true))
        }.onSuccess {
            val isFirst = _pageIndex == 0
            _pageIndex++
            _articlesFlow.emit(
                DataState.Success(
                    ArticleListState(
                        isFirst,
                        it.over,
                        it.datas
                    )
                )
            )
        }.onError {
            _articlesFlow.emit(DataState.Error(it.message ?: "网络错误"))
        }.onFinally {
            _articlesFlow.emit(DataState.Loading(false))
        }
    }

    private fun removeHtmlTag(content: String): String {
        val regEx = "<em class='highlight'>|</em>"
        return content.replace(Regex(regEx), "")
    }

    private fun getHotKey() {
        execute {
            apiService.hotKey()
        }.onSuccess {
            if (it.isSuccess) {
                hotKeyAdapter.submitList(it.data)
            }
        }
    }

    private fun getSearchHistory() {
        execute {
            val historyKey = stringPreferencesKey("search_history")
            val historyJson = appCtx.dataStore.data
                .map { preferences ->
                    preferences[historyKey] ?: "[]"
                }.first()
            val gson = Gson()
            val histories: List<String> =
                gson.fromJson(historyJson, object : TypeToken<List<String>>() {}.type)
            histories
        }.onSuccess { data ->
            searchHistoryAdapter.submitList(data)
            _searchStateFlow.update {
                it.copy(isSearchHistoryEmpty = data.isEmpty())
            }
        }.onError {
            Log.e(TAG, "getSearchHistory error: ${it.message}")
        }
    }

    private fun updateSearchHistory(key: String) {
        val exH = object : CoroutineExceptionHandler {
            override val key: CoroutineContext.Key<*>
                get() = CoroutineExceptionHandler

            override fun handleException(context: CoroutineContext, exception: Throwable) {
                Log.e(TAG, "updateSearchHistory error: ${exception.message}")
            }
        }
        viewModelScope.launch(Dispatchers.IO + exH) {
            val historyKey = stringPreferencesKey("search_history")
            val historyJson = appCtx.dataStore.data
                .map { preferences ->
                    preferences[historyKey] ?: "[]"
                }.first()
            val gson = Gson()
            val histories: MutableList<String> =
                gson.fromJson(historyJson, object : TypeToken<MutableList<String>>() {}.type)
            histories.remove(key)
            histories.add(0, key)
            val saveList = if (histories.size > 10) {
                histories.subList(0, 10)
            } else histories

            appCtx.dataStore.edit { settings ->
                settings[historyKey] = gson.toJson(saveList)
            }
            withContext(Dispatchers.Main) {
                searchHistoryAdapter.submitList(saveList)
            }
            _searchStateFlow.update {
                it.copy(isSearchHistoryEmpty = false)
            }
        }
    }

    fun clearSearchHistory() {
        execute {
            val historyKey = stringPreferencesKey("search_history")
            appCtx.dataStore.edit { settings ->
                settings.remove(historyKey)
            }
        }.onSuccess {
            searchHistoryAdapter.submitList(emptyList())
            _searchStateFlow.update {
                it.copy(isSearchHistoryEmpty = true)
            }
        }
    }
}
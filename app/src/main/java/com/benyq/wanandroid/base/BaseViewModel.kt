package com.benyq.wanandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.coroutine.Coroutine
import com.benyq.wanandroid.base.network.ApiResponse
import com.benyq.wanandroid.ui.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    protected val TAG = this::class.java.simpleName

    protected fun <T> execute(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        executeContext: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.() -> T
    ): Coroutine<T> {
        return Coroutine.async(scope, context, start, executeContext, block)
    }

    protected fun <R> submit(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Deferred<R>
    ): Coroutine<R> {
        return Coroutine.async(scope, context) { block().await() }
    }

    protected fun <T> flowResponse(defaultValue: T? = null, request: suspend () -> ApiResponse<T>): Flow<DataState<T>> {
        return flow {
            val response = request()
            if (response.isSuccess || defaultValue != null){
                emit(DataState.Success<T>(response.data ?: defaultValue!!))
            }else {
                emit(DataState.Error<T>(response.errorMsg))
            }
        }.flowOn(Dispatchers.IO)
            .onStart { emit(DataState.Loading(true)) }
            .catch { DataState.Error<T>(it.message ?: "网络错误") }
            .onCompletion {
                emit(DataState.Loading<T>(false))
            }
    }
}
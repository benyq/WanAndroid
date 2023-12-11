package com.benyq.wanandroid.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.wanandroid.base.coroutine.Coroutine
import com.benyq.wanandroid.base.network.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    fun <T> execute(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        executeContext: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.() -> T
    ): Coroutine<T> {
        return Coroutine.async(scope, context, start, executeContext, block)
    }

    fun <R> submit(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Deferred<R>
    ): Coroutine<R> {
        return Coroutine.async(scope, context) { block().await() }
    }
}
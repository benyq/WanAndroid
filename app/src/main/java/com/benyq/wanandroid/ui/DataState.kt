package com.benyq.wanandroid.ui

/**
 *
 * @author benyq
 * @date 12/12/2023
 *
 */
sealed class DataState<T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error<T>(val error: String) : DataState<T>()
    data class Loading<T>(val loading: Boolean = false) : DataState<T>()
}
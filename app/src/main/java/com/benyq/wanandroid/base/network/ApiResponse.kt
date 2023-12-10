package com.benyq.wanandroid.base.network

data class ApiResponse<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T?
) {
    val isSuccess: Boolean get() = errorCode == 0 && data!= null
}
package com.benyq.wanandroid.base.network

data class ApiResponse<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T?
) {
    val isSuccess: Boolean get() = errorCode == 0 && data!= null

    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(0, "", data)
        }

        fun <T> error(errorCode: Int, errorMsg: String): ApiResponse<T> {
            return ApiResponse(errorCode, errorMsg, null)
        }
    }
}
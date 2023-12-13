package com.benyq.wanandroid.ui.login

import android.util.Log
import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.LoginModel
import com.benyq.wanandroid.ui.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *
 * @author benyq
 * @date 12/11/2023
 *
 */


class LoginViewModel : BaseViewModel() {

    var username: String = ""
    var password: String = ""

    fun login(): Flow<DataState<LoginModel>> {
        return flowResponse {
            apiService.login(username, password)
        }
    }


}
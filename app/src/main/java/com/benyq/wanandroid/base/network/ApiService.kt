package com.benyq.wanandroid.base.network

import com.benyq.wanandroid.base.extensions.appCtx
import com.benyq.wanandroid.model.ArticleModel
import com.benyq.wanandroid.model.BannerModel
import com.benyq.wanandroid.model.CategoryTreeModel
import com.benyq.wanandroid.model.HotKeyModel
import com.benyq.wanandroid.model.LoginModel
import com.benyq.wanandroid.model.PageModel
import com.benyq.wanandroid.model.UserInfoModel
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("banner/json")
    suspend fun banner(): ApiResponse<List<BannerModel>>

    @GET("article/list/{page}/json")
    suspend fun articles(@Path("page") page: Int, @Query("cid") cid: Int? = null): ApiResponse<PageModel<ArticleModel>>

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): ApiResponse<LoginModel>

    @GET("user/lg/userinfo/json")
    suspend fun userInfo(): ApiResponse<UserInfoModel>

    @GET("hotkey/json")
    suspend fun hotKey(): ApiResponse<List<HotKeyModel>>

    @GET("tree/json")
    suspend fun categoryTree(): ApiResponse<List<CategoryTreeModel>>
}


val cookieJar by lazy {
    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appCtx))
}
val apiService by lazy { RetrofitCreator().createRetrofit() }

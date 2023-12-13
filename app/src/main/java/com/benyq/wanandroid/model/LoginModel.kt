package com.benyq.wanandroid.model


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("admin")
    val admin: Boolean,
    @SerializedName("chapterTops")
    val chapterTops: List<Any>,
    @SerializedName("coinCount")
    val coin: String,
    @SerializedName("collectIds")
    val collectIds: List<Int>,
    @SerializedName("email")
    val email: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("publicName")
    val publicName: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("username")
    val username: String
)
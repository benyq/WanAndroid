package com.benyq.wanandroid.model


import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    @SerializedName("coinInfo")
    val coinInfo: Coin,
    @SerializedName("userInfo")
    val userInfo: User
) {
    data class Coin(
        @SerializedName("coinCount")
        val coin: String,
        @SerializedName("level")
        val level: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )

    data class User(
        @SerializedName("admin")
        val admin: Boolean,
        @SerializedName("chapterTops")
        val chapterTops: List<Any>,
        @SerializedName("coinCount")
        val coinCount: Int,
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
}
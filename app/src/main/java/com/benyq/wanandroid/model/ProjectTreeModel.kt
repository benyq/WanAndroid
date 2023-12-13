package com.benyq.wanandroid.model


import com.google.gson.annotations.SerializedName

data class ProjectTreeModel(
    @SerializedName("articleList")
    val articleList: List<Any>,
    @SerializedName("author")
    val author: String,
    @SerializedName("children")
    val children: List<Any>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lisense")
    val lisense: String,
    @SerializedName("lisenseLink")
    val lisenseLink: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerializedName("visible")
    val visible: Int
)
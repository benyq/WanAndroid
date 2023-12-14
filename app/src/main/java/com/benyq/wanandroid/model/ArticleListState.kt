package com.benyq.wanandroid.model

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */
data class ArticleListState(
    val isFirst: Boolean = true,
    val isEnd: Boolean = false,
    val articles: List<ArticleModel> = emptyList(),
)

package com.benyq.wanandroid.ui.category

import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.CategoryTreeModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel: BaseViewModel() {

    private val _categoryTreeFlow = MutableStateFlow<List<CategoryTreeModel>>(emptyList())
    val categoryTreeFlow: StateFlow<List<CategoryTreeModel>> = _categoryTreeFlow

    init {
        getCategoryTree()
    }

    private fun getCategoryTree() {
        execute {
            apiService.categoryTree()
        }.onSuccess {
            if (it.isSuccess) {
                _categoryTreeFlow.emit(it.data!!)
            }
        }
    }
}
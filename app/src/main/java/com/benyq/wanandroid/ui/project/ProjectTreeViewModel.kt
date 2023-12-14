package com.benyq.wanandroid.ui.project

import com.benyq.wanandroid.base.BaseViewModel
import com.benyq.wanandroid.base.network.apiService
import com.benyq.wanandroid.model.ProjectTreeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * @author benyq
 * @date 12/13/2023
 *
 */
class ProjectTreeViewModel : BaseViewModel() {

    private val _tree = MutableStateFlow<List<ProjectTreeModel>>(emptyList())
    val tree: StateFlow<List<ProjectTreeModel>> = _tree

    init {
        getTree()
    }

    private fun getTree() {
        execute {
            apiService.projectTree()
        }.onSuccess {
            if (it.isSuccess) {
                _tree.emit(it.data!!)
            }
        }
    }
}
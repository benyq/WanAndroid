package com.benyq.wanandroid.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentFragment: String
        get() = savedStateHandle["currentFragment"] ?: "HomeFragment"
        set(value) {
            savedStateHandle["currentFragment"] = value
        }

}
package com.benyq.wanandroid.base.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 *
 * @author benyq
 * @date 11/28/2023
 *
 */
abstract class BaseFragment<VB : ViewBinding>(@LayoutRes val layoutId: Int): Fragment(layoutId) {


    private var _dataBind: VB? = null
    protected val dataBind: VB get() = checkNotNull(_dataBind) { "初始化binding失败" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _dataBind = getViewBinding(view)
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _dataBind = null
            }
        })
        onFragmentViewCreated(savedInstanceState)
        observe()
        initData()
    }

    abstract fun getViewBinding(view: View): VB
    abstract fun onFragmentViewCreated(savedInstanceState: Bundle?)
    abstract fun observe()
    open fun initData(){}
}
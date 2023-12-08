package com.benyq.wanandroid.base.ui

import android.os.Bundle
import android.util.Log
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

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private var _dataBind: VB? = null
    protected val binding: VB get() = checkNotNull(_dataBind) { "初始化binding失败" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: $this")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $this")
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: $this")
    }

    abstract fun getViewBinding(view: View): VB
    abstract fun onFragmentViewCreated(savedInstanceState: Bundle?)
    abstract fun observe()
    open fun initData(){}
}
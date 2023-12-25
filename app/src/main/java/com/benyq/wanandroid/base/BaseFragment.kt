package com.benyq.wanandroid.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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

    protected val TAG = this::class.java.simpleName

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
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                _dataBind = null
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            onApplyWindow(view, windowInsets)
        }
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
    open fun onApplyWindow(view: View, windowInsets: WindowInsetsCompat): WindowInsetsCompat{
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        return WindowInsetsCompat.CONSUMED
    }
}
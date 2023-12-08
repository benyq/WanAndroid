package com.benyq.wanandroid.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    protected val binding: VB by lazy {
        getViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onActivityCreated()
        observe()
    }


    abstract fun getViewBinding(): VB

    abstract fun onActivityCreated()

    open fun observe(){}
}
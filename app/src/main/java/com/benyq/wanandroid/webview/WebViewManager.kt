package com.benyq.wanandroid.webview

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.view.ViewGroup

/**
 *
 * @author benyq
 * @date 12/11/2023
 *
 */
/**
 * WebView的缓存容器
 * obtail获取对象
 * recycle回收对象
 */
object WebViewManager {

    private val webViewCache: MutableList<RobustWebView> = ArrayList(1)

    private fun create(context: Context): RobustWebView {
        return RobustWebView(context)
    }

    fun init(context: Context) {
        //空闲的时候初始化WebView容器
        Looper.myQueue().addIdleHandler {
            //初始化WebView缓存容器
            prepare(context)
            false
        }
    }

    /**
     * 初始化
     */
    @JvmStatic
    private fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewCache.add(create(MutableContextWrapper(context)))
                false
            }
        }
    }

    /**
     * 获取WebView
     */
    @JvmStatic
    fun obtain(context: Context): RobustWebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        val webView = webViewCache.removeFirst()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        webView.clearHistory()
        webView.resumeTimers()
        return webView
    }

    /**
     * 回收资源
     */
    @JvmStatic
    fun recycle(webView: RobustWebView) {
        try {
            webView.stopLoading()
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.pauseTimers()
            webView.clearFormData()
            webView.removeJavascriptInterface("webkit")

            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    /**
     * 销毁资源
     */
    @JvmStatic
    fun destroy() {
        try {
            webViewCache.forEach {
                it.removeAllViews()
                it.destroy()
                webViewCache.remove(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
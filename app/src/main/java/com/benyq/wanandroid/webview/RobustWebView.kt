package com.benyq.wanandroid.webview

import android.content.Context
import android.content.MutableContextWrapper
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File

/**
 * @Author: leavesCZY
 * @Date: 2021/9/20 22:45
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
interface WebViewListener {

    fun onProgressChanged(webView: RobustWebView, progress: Int) {

    }

    fun onReceivedTitle(webView: RobustWebView, title: String) {

    }

    fun onPageFinished(webView: RobustWebView, url: String) {

    }

}

class RobustWebView(context: Context, attributeSet: AttributeSet? = null) :
    WebView(context, attributeSet) {

    private val baseCacheDir by lazy {
        File(context.cacheDir, "webView")
    }

    private val databaseCachePath by lazy {
        File(baseCacheDir, "databaseCache").absolutePath
    }

    private val appCachePath by lazy {
        File(baseCacheDir, "appCache").absolutePath
    }

    var hostLifecycleOwner: LifecycleOwner? = null

    var webViewListener: WebViewListener? = null

    private val mWebChromeClient = object : WebChromeClient() {

        override fun onProgressChanged(webView: WebView, newProgress: Int) {
            super.onProgressChanged(webView, newProgress)
            Log.d("RobustWebView","onProgressChanged-$newProgress")
            webViewListener?.onProgressChanged(this@RobustWebView, newProgress)
        }

        override fun onReceivedTitle(webView: WebView, title: String?) {
            super.onReceivedTitle(webView, title)
            Log.d("RobustWebView","onReceivedTitle-$title")
            webViewListener?.onReceivedTitle(this@RobustWebView, title ?: "")
        }

        override fun onJsAlert(
            webView: WebView,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            Log.d("RobustWebView","onJsAlert: $webView $message")
            return super.onJsAlert(webView, url, message, result)
        }

        override fun onJsConfirm(
            webView: WebView,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            Log.d("RobustWebView","onJsConfirm: $url $message")
            return super.onJsConfirm(webView, url, message, result)
        }

        override fun onJsPrompt(
            webView: WebView,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
            Log.d("RobustWebView","onJsPrompt: $url $message $defaultValue")
            return super.onJsPrompt(webView, url, message, defaultValue, result)
        }
    }

    private val mWebViewClient = object : WebViewClient() {

        private var startTime = 0L

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onPageStarted(webView: WebView, url: String?, favicon: Bitmap?) {
            super.onPageStarted(webView, url, favicon)
            startTime = System.currentTimeMillis()
        }

        override fun onPageFinished(webView: WebView, url: String?) {
            super.onPageFinished(webView, url)
            Log.d("RobustWebView","onPageFinished-$url")
            webViewListener?.onPageFinished(this@RobustWebView, url ?: "")
            Log.d("RobustWebView","onPageFinished duration： " + (System.currentTimeMillis() - startTime))
        }

        override fun onReceivedSslError(
            webView: WebView,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Log.d("RobustWebView","onReceivedSslError-$error")
            super.onReceivedSslError(webView, handler, error)
        }

        override fun shouldInterceptRequest(webView: WebView, url: String): WebResourceResponse? {
            return super.shouldInterceptRequest(webView, url)
        }

        override fun shouldInterceptRequest(
            webView: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(webView, request)
        }
    }

    init {
        webViewClient = mWebViewClient
        webChromeClient = mWebChromeClient
        initWebViewSettings(this)
        setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Log.d("RobustWebView"
                ,"setDownloadListener: $url \n" +
                        "$userAgent \n " +
                        " $contentDisposition \n" +
                        " $mimetype \n" +
                        " $contentLength"
            )
        }
    }

    fun toLoadUrl(url: String, cookie: String) {
        val mCookieManager = CookieManager.getInstance()
        mCookieManager?.setCookie(url, cookie)
        mCookieManager?.flush()
        loadUrl(url)
    }

    fun toGoBack(): Boolean {
        if (canGoBack()) {
            goBack()
            return false
        }
        return true
    }

    private fun initWebViewSettings(webView: WebView) {
        val settings = webView.settings

//        settings.userAgentString = "android-leavesCZY"
        settings.javaScriptEnabled = true

        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.loadsImagesAutomatically = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.databasePath = databaseCachePath
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        overScrollMode = OVER_SCROLL_NEVER
        isFocusable = true
        isHorizontalScrollBarEnabled = false
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("RobustWebView","onAttachedToWindow : $context")
        (hostLifecycleOwner ?: findLifecycleOwner(context))?.let {
            addHostLifecycleObserver(it)
        }
    }

    private fun findLifecycleOwner(context: Context): LifecycleOwner? {
        if (context is LifecycleOwner) {
            return context
        }
        if (context is MutableContextWrapper) {
            val baseContext = context.baseContext
            if (baseContext is LifecycleOwner) {
                return baseContext
            }
        }
        return null
    }

    private fun addHostLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        Log.d("RobustWebView","addLifecycleObserver")
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                onHostResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onHostPause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                onHostDestroy()
            }
        })
    }

    private fun onHostResume() {
        Log.d("RobustWebView","onHostResume")
        onResume()
    }

    private fun onHostPause() {
        Log.d("RobustWebView","onHostPause")
        onPause()
    }

    private fun onHostDestroy() {
        Log.d("RobustWebView","onHostDestroy")
        release()
    }

    private fun release() {
        hostLifecycleOwner = null
        webViewListener = null
        webChromeClient = null
        (parent as? ViewGroup)?.removeView(this)
        destroy()
    }

}
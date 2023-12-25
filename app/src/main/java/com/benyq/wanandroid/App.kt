package com.benyq.wanandroid

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.benyq.wanandroid.base.extensions.appCtx
import com.benyq.wanandroid.webview.WebViewManager

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        appCtx = this
        WebViewManager.init(this)
    }
}
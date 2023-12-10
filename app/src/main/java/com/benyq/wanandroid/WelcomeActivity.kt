package com.benyq.wanandroid

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.benyq.wanandroid.base.BaseActivity
import com.benyq.wanandroid.databinding.ActivityWelcomeBinding
import com.benyq.wanandroid.ui.MainActivity

/**
 * @author benyq
 * @date 12/8/2023
 * 显示完SplashScreen之后，可以展示一些业务UI，如广告
 * 但是目前没有，所以直接下个页面
 */
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    override fun getViewBinding() = ActivityWelcomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityCreated() {
    }
}
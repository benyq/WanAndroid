package com.benyq.wanandroid.base.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.prefs.Preferences
import kotlin.coroutines.resume

/**
 *
 * @author benyq
 * @date 8/23/2023
 *
 */

lateinit var appCtx: Application

fun Activity.showLoading() {
    contentView()?.apply {
        val pb = ProgressBar(context)
        pb.tag = "pb"
        addView(pb, FrameLayout.LayoutParams(150, 150, Gravity.CENTER))
    }
}

fun Activity.hideLoading() {
    val pb = contentView()?.findViewWithTag<ProgressBar>("pb")
    pb?.let { contentView()?.removeView(it) }
}

fun Activity.contentView(): FrameLayout? {
    return takeIf { !isFinishing && !isDestroyed }?.window?.decorView?.findViewById(android.R.id.content)
}

fun Activity.systemBarColor(@ColorInt color: Int) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    window.statusBarColor = color
    window.navigationBarColor = color
    val luminance = ColorUtils.calculateLuminance(color)
    insetsController.isAppearanceLightStatusBars = luminance > 0.5
}

fun Activity.isAppearanceLightStatusBars(isLight: Boolean) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.isAppearanceLightStatusBars = isLight
}

fun Activity.statusBarColor(@ColorInt color: Int) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    window.statusBarColor = color
    val luminance = ColorUtils.calculateLuminance(color)
    insetsController.isAppearanceLightStatusBars = luminance > 0.5
}

fun Activity.fullScreen(fullScreen: Boolean = true) {
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    if (fullScreen) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        //允许window 的内容可以上移到刘海屏状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }else {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        insetsController.show(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }

}

fun Fragment.systemBarColor(@ColorInt color: Int) {
    requireActivity().systemBarColor(color)
}

fun Fragment.statusBarColor(@ColorInt color: Int) {
    requireActivity().statusBarColor(color)
}

fun Fragment.fullScreen(fullScreen: Boolean = true) {
    requireActivity().fullScreen(fullScreen)
}


fun Fragment.getColor(@ColorRes id: Int) = requireActivity().getColor(id)

suspend fun Context.alert(title: String, message: String): Boolean =
    suspendCancellableCoroutine { continuation ->
        AlertDialog.Builder(this)
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
                continuation.resume(false)
            }.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                continuation.resume(true)
            }.setTitle(title)
            .setMessage(message)
            .setOnCancelListener {
                continuation.resume(false)
            }.create()
            .also { dialog ->
                continuation.invokeOnCancellation {
                    dialog.dismiss()
                }
            }.show()
    }


private val mainHandler = Handler(Looper.getMainLooper())
fun showToast(message: String) {
    fun toast(message: String) {
        Toast.makeText(appCtx, message, Toast.LENGTH_SHORT).show()
    }
    if (mainHandler.looper.isCurrentThread) {
        toast(message)
    }else {
        mainHandler.post {
            toast(message)
        }
    }
}

val Context.dataStore by preferencesDataStore(name = "settings")


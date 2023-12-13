package com.benyq.wanandroid.base.extensions

import android.content.res.Resources
import android.util.TypedValue

/**
 *
 * @author benyq
 * @date 8/9/2023
 *
 */

/**
 * dp to px
 */
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density + 0.5f

val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics) + 0.5f
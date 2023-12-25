package com.benyq.wanandroid.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.use
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.extensions.dp

/**
 *
 * @author benyq
 * @date 12/18/2023
 *
 */
class SettingCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val ivIcon: AppCompatImageView
    private val tvTitle: AppCompatTextView
    private val tvContent: AppCompatTextView

    init {
        gravity = Gravity.CENTER_VERTICAL
        ivIcon = AppCompatImageView(context)
        tvTitle = AppCompatTextView(context)
        tvContent = AppCompatTextView(context)

        ivIcon.layoutParams = LayoutParams(30.dp, 30.dp)
        addView(ivIcon)

        tvTitle.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f).apply {
            marginStart = 15.dp
        }
        tvTitle.textSize = 15f
        tvTitle.gravity = Gravity.CENTER_VERTICAL
        tvTitle.setTextColor(Color.BLACK)
        addView(tvTitle)

        tvContent.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
            marginStart = 10.dp
            marginEnd = 10.dp
        }
        tvContent.gravity = Gravity.CENTER_VERTICAL
        tvContent.textSize = 15f
        tvContent.setTextColor(Color.BLACK)
        addView(tvContent)

        val imageArrow = AppCompatImageView(context)
        imageArrow.setImageResource(R.drawable.ic_arrow_forward)
        imageArrow.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(imageArrow)

        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.SettingCellView)
        }?.use {
            val title = it.getString(R.styleable.SettingCellView_cell_title)
            val content = it.getString(R.styleable.SettingCellView_cell_content)
            val icon = it.getDrawable(R.styleable.SettingCellView_cell_icon)

            tvTitle.text = title
            tvContent.text = content
            ivIcon.setImageDrawable(icon)
        }

    }

    fun setContent(content: String?) {
        tvContent.text = content
    }
}
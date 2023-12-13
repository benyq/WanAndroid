package com.benyq.wanandroid.ui.category

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benyq.wanandroid.base.extensions.dp
import com.benyq.wanandroid.base.extensions.sp
import com.benyq.wanandroid.databinding.ItemCategoryTreeBinding
import com.benyq.wanandroid.model.CategoryTreeModel
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.flexbox.FlexboxLayout

/**
 *
 * @author benyq
 * @date 12/12/2023
 *
 */
class CategoryTreeAdapter(private val action: (CategoryTreeModel) -> Unit) :
    BaseQuickAdapter<CategoryTreeModel, CategoryTreeAdapter.CategoryTreeVH>() {
    class CategoryTreeVH(val itemVB: ItemCategoryTreeBinding) : RecyclerView.ViewHolder(itemVB.root)

    private val _colors = listOf(
        "#FFF44336",
        "#FFE91E63",
        "#FF9C27B0",
        "#FF673AB7",
        "#FF3F51B5",
        "#FF2196F3",
        "#FF03A9F4",
        "#FF00BCD4",
        "#FF009688",
        "#FF4CAF50",
        "#FF8BC34A",
    )

    override fun onBindViewHolder(holder: CategoryTreeVH, position: Int, item: CategoryTreeModel?) {
        holder.itemVB.tvTitle.text = item?.name
        holder.itemVB.flexLayout.removeAllViews()
        item?.children?.map { model ->
            val textView = TextView(holder.itemView.context)
            textView.text = model.name
            textView.setTextColor(Color.WHITE)
            textView.setOnClickListener {
                action(model)
            }
            textView.textSize = 16f
            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE // 矩形
                cornerRadius = 10f.dp // 圆角
                val color = Color.parseColor(_colors[model.id % _colors.size])
                colors = intArrayOf(color, color)
            }
            textView.setPadding(20.dp, 10.dp, 20.dp, 10.dp)
            textView.layoutParams = layoutParams
            holder.itemVB.flexLayout.addView(textView)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): CategoryTreeVH {
        return CategoryTreeVH(
            ItemCategoryTreeBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }
}
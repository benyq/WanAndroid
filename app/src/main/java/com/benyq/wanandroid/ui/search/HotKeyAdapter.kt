package com.benyq.wanandroid.ui.search

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.benyq.wanandroid.R
import com.benyq.wanandroid.model.HotKeyModel
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import kotlin.math.abs

/**
 *
 * @author benyq
 * @date 12/14/2023
 *
 */

private val colors = listOf(
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
class HotKeyAdapter(private val action: (String)->Unit): BaseQuickAdapter<HotKeyModel, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: HotKeyModel?) {
        item?.let {
            holder.setText(R.id.tv_name, it.name)
            holder.setBackgroundColor(R.id.iv_background, Color.parseColor(colors[abs(it.hashCode()) % colors.size]))
            holder.itemView.setOnClickListener {
                action(item.name)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_search_hisotry_hotkey, parent)
    }
}
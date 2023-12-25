package com.benyq.wanandroid.ui.mine.language

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.getColor
import com.benyq.wanandroid.base.extensions.gone
import com.benyq.wanandroid.base.extensions.invisible
import com.benyq.wanandroid.base.extensions.showToast
import com.benyq.wanandroid.base.extensions.statusBarColor
import com.benyq.wanandroid.base.extensions.visible
import com.benyq.wanandroid.databinding.FragmentLanguageBinding
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder

/**
 *
 * @author benyq
 * @date 12/18/2023
 *
 */
class LanguageFragment : BaseFragment<FragmentLanguageBinding>(R.layout.fragment_language) {
    override fun getViewBinding(view: View) = FragmentLanguageBinding.bind(view)

    private val viewModel by viewModels<LanguageViewModel>(ownerProducer = { requireActivity() })

    private var currentLanguage: LanguageModel? = null
    private val languageAdapter = object : BaseQuickAdapter<LanguageModel, QuickViewHolder>() {
        override fun onBindViewHolder(
            holder: QuickViewHolder,
            position: Int,
            item: LanguageModel?,
        ) {
            holder.itemView.setOnClickListener {
                currentLanguage = item
                notifyDataSetChanged()
            }
            if (currentLanguage == item) {
                holder.getView<ImageView>(R.id.iv_check).visible()
            } else {
                holder.getView<ImageView>(R.id.iv_check).invisible()
            }
            holder.getView<TextView>(R.id.tv_content).text = item?.name
        }

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int,
        ): QuickViewHolder {
            return QuickViewHolder(R.layout.item_language, parent)
        }
    }

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        statusBarColor(getColor(R.color.color_background))
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnApply.setOnClickListener {
            applyLanguage()
        }
        binding.rvLanguage.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvLanguage.adapter = languageAdapter

        currentLanguage = viewModel.currentLanguage
        languageAdapter.submitList(viewModel.supportLanguage)
    }

    override fun observe() {

    }

    private fun applyLanguage() {
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.language_change_confirm)
            .setPositiveButton(R.string.confirm) { dialog, which ->
                currentLanguage?.let {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(it.language))
                } ?: showToast(getString(R.string.language_not_selected))
            }
            .setNegativeButton(R.string.cancel, null)
            .create().show()
    }

}
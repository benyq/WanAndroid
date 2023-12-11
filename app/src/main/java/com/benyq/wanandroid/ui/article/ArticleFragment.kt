package com.benyq.wanandroid.ui.article

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.databinding.FragmentArticleBinding
import com.benyq.wanandroid.webview.RobustWebView
import com.benyq.wanandroid.webview.WebViewManager

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class ArticleFragment: BaseFragment<FragmentArticleBinding>(R.layout.fragment_article) {

    private val windowsUA =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 Edg/98.0.1108.55"
    private lateinit var webView: RobustWebView

    override fun getViewBinding(view: View) = FragmentArticleBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val webBackForwardList = webView.copyBackForwardList()
                val historyOneOriginalUrl = webBackForwardList.getItemAtIndex(0)?.originalUrl
                val curIndex = webBackForwardList.currentIndex

                // https://mp.weixin.qq.com/s/GJJHjbg9QK93kD4YzHnrAQ
                // 判断是否是缓存的WebView
                if (historyOneOriginalUrl?.contains("data:text/html;charset=utf-8") == true) {
                    //说明是缓存复用的的WebView
                    if (curIndex > 1) {
                        //内部跳转到另外的页面了，可以返回的
                        webView.goBack()
                    } else {
                        findNavController().navigateUp()
                    }
                } else {
                    //如果不是缓存复用的WebView，可以直接返回
                    if (webView.toGoBack()) {
                        findNavController().navigateUp()
                    }
                }
            }
        })
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        webView = WebViewManager.obtain(requireActivity())  //管理类获取对象
        webView.loadUrl(arguments?.getString("url")?: "")
        webView.layoutParams = params
        binding.root.addView(webView)

        arguments?.getString("title")?.let {
            binding.tvTitle.text = it
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun observe() {
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WebViewManager.recycle(webView)
    }

}
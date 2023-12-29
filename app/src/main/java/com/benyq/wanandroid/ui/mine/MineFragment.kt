package com.benyq.wanandroid.ui.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.getColor
import com.benyq.wanandroid.databinding.FragmentMineBinding
import com.benyq.wanandroid.ui.ShareViewModel
import com.benyq.wanandroid.ui.mine.language.LanguageViewModel

/**
 *
 * @author benyq
 * @date 12/8/2023
 *
 */
class MineFragment : BaseFragment<FragmentMineBinding>(R.layout.fragment_mine) {

    private val shareViewModel by viewModels<ShareViewModel>(ownerProducer = { requireActivity() })
    private val languageViewModel by viewModels<LanguageViewModel>(ownerProducer = { requireActivity() })

    override fun getViewBinding(view: View) = FragmentMineBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        binding.cvUser.setOnClickListener {
            findNavController().navigate(R.id.action_mine_to_login)
        }
        binding.btnLogout.setOnClickListener {
            shareViewModel.logout()
        }
        binding.llCollect.setOnClickListener {
            findNavController().navigate(R.id.action_mine_to_user_collect)
        }
        binding.scLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_mine_to_language)
        }
        binding.scLanguage.setContent(languageViewModel.currentLanguage?.name)
    }

    override fun observe() {
        shareViewModel.userInfo.collectOnLifecycle(viewLifecycleOwner) {
            binding.tvNickname.text = it.nickname
            binding.tvCollect.text = it.collectIds.size.toString()
            binding.tvCoin.text = it.coin
            binding.tvRank.text = it.rank
        }
    }
}
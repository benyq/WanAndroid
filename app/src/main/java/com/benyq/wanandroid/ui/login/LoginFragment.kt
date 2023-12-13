package com.benyq.wanandroid.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.benyq.wanandroid.R
import com.benyq.wanandroid.base.BaseFragment
import com.benyq.wanandroid.base.extensions.collectOnLifecycle
import com.benyq.wanandroid.base.extensions.hideLoading
import com.benyq.wanandroid.base.extensions.showLoading
import com.benyq.wanandroid.base.extensions.statusBarColor
import com.benyq.wanandroid.databinding.FragmentLoginBinding
import com.benyq.wanandroid.ui.DataState
import com.benyq.wanandroid.ui.ShareViewModel

/**
 *
 * @author benyq
 * @date 12/11/2023
 *
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>(ownerProducer = { this })
    private val shareViewModel by viewModels<ShareViewModel>(ownerProducer = { requireActivity() })

    override fun getViewBinding(view: View) = FragmentLoginBinding.bind(view)

    override fun onFragmentViewCreated(savedInstanceState: Bundle?) {
        statusBarColor(requireActivity().getColor(R.color.teal_700))
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnLogin.setOnClickListener {
            viewModel.login()
                .collectOnLifecycle(viewLifecycleOwner) {
                    Log.d(TAG, "onFragmentViewCreated: login: $it")
                    when(it) {
                        is DataState.Loading -> {
                            if (it.loading) {
                                requireActivity().showLoading()
                            }else {
                                requireActivity().hideLoading()
                            }
                        }
                        is DataState.Error -> {

                        }
                        is DataState.Success -> {
                            with(it.data) {
                                shareViewModel.updateUserInfo(nickname, coin, collectIds)
                                //开启一个获取用户信息的任务
                                shareViewModel.queryUserInfo()
                            }
                            findNavController().navigateUp()
                        }
                    }
                }
        }
        binding.etNickname.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            onTextChanged()
        })
        binding.etPassword.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            onTextChanged()
        })

    }

    override fun observe() {
    }

    private fun onTextChanged() {
        viewModel.username = binding.etNickname.text.toString().trim()
        viewModel.password = binding.etPassword.text.toString().trim()

        binding.btnLogin.isEnabled =
            viewModel.username.isNotEmpty() && viewModel.password.isNotEmpty()
    }
}
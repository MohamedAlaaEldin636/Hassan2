package com.maproductions.mohamedalaa.shared.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.databinding.FragmentVerifyPhoneBinding
import com.maproductions.mohamedalaa.shared.presentation.auth.viewModel.VerifyPhoneViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyPhoneFragment : MABaseFragment<FragmentVerifyPhoneBinding>() {

    companion object {
        const val SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON = "SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON"
    }

    private val viewModel by viewModels<VerifyPhoneViewModel>()

    private var firstText = ""
    private var secondText = ""
    private var thirdText = ""
    private var fourthText = ""

    private val beforeTextChange: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ ->
        firstText = viewModel.firstConfirmText.value.orEmpty()
        secondText = viewModel.secondConfirmText.value.orEmpty()
        thirdText = viewModel.thirdConfirmText.value.orEmpty()
        fourthText = viewModel.fourthConfirmText.value.orEmpty()
    }
    private val watcher: (Editable?) -> Unit = { _ ->
        if (fourthText.isNotEmpty() && viewModel.fourthConfirmText.value.isNullOrEmpty()) {
            binding?.thirdTextInputEditText?.requestFocus()
            binding?.thirdTextInputEditText?.post {
                binding?.thirdTextInputEditText?.setSelection(0, viewModel.thirdConfirmText.value.orEmpty().length)
            }
        }else if (thirdText.isNotEmpty() && viewModel.thirdConfirmText.value.isNullOrEmpty()) {
            binding?.secondTextInputEditText?.requestFocus()
            binding?.secondTextInputEditText?.post {
                binding?.secondTextInputEditText?.setSelection(0, viewModel.secondConfirmText.value.orEmpty().length)
            }
        }else if (secondText.isNotEmpty() && viewModel.secondConfirmText.value.isNullOrEmpty()) {
            binding?.firstTextInputEditText?.requestFocus()
            binding?.firstTextInputEditText?.post {
                binding?.firstTextInputEditText?.setSelection(0, viewModel.firstConfirmText.value.orEmpty().length)
            }
        }else {
            if (!viewModel.firstConfirmText.value.isNullOrEmpty()) {
                if (!viewModel.secondConfirmText.value.isNullOrEmpty()) {
                    if (!viewModel.thirdConfirmText.value.isNullOrEmpty()) {
                        if (!viewModel.fourthConfirmText.value.isNullOrEmpty()) {
                            // Do nothing.
                        }else {
                            binding?.fourthTextInputEditText?.requestFocus()
                        }
                    }else {
                        binding?.thirdTextInputEditText?.requestFocus()
                    }
                }else {
                    binding?.secondTextInputEditText?.requestFocus()
                }
            }else {
                binding?.firstTextInputEditText?.requestFocus()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_verify_phone

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.firstTextInputEditText?.addTextChangedListener(beforeTextChange) { watcher(it) }
        binding?.secondTextInputEditText?.addTextChangedListener(beforeTextChange) { watcher(it) }
        binding?.thirdTextInputEditText?.addTextChangedListener(beforeTextChange) { watcher(it) }
        binding?.fourthTextInputEditText?.addTextChangedListener(beforeTextChange) { watcher(it) }

        if (viewModel.args.isForgetPassword) {
            activityViewModel?.titleToolbar?.postValue(getString(R.string.forget_password))

            binding?.textView?.text = getString(R.string.enter_verification_code_instruction_2)
        }
    }

}

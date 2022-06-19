package com.maproductions.mohamedalaa.shared.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.databinding.FragmentMsgFormBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.settings.viewModel.MSGFormViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MSGFormFragment : MABaseFragment<FragmentMsgFormBinding>() {

    private val viewModel by viewModels<MSGFormViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_msg_form

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(viewModel.args.title)
    }

}

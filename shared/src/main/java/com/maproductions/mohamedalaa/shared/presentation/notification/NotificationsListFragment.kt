package com.maproductions.mohamedalaa.shared.presentation.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.databinding.FragmentNotificationsListBinding
import com.maproductions.mohamedalaa.shared.databinding.FragmentOnBoardBinding
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.notification.viewModel.NotificationsListViewModel
import com.maproductions.mohamedalaa.shared.presentation.onBoard.viewModel.OnBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsListFragment : MABaseFragment<FragmentNotificationsListBinding>() {

    private val viewModel by viewModels<NotificationsListViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_notifications_list

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notifications.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

}

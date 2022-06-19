package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentSpecializationsAndServicesBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentWalletBinding
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.SpecializationsAndServicesViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.WalletViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class WalletFragment : MABaseFragment<FragmentWalletBinding>() {

    private val viewModel by viewModels<WalletViewModel>()

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameWallet(
                runBlocking { prefsAccount.getProviderData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_WALLET
        ) { data ->
            Timber.e("wallet ->\n$data")

            Handler(Looper.getMainLooper()).post {
                viewModel.adapter.refresh()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_wallet

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            viewModel.totalValueOfWallet.value = "${it.data?.currentAmount.orZero()} ${getString(SR.string.egp)}"

            viewModel.info.value = getString(SR.string.wallet_specific_limit_var, it.data?.limit.orZero().toString())
        }

        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.walletHistory.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}

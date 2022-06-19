package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentAcceptedProviderServiceRequestBinding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentPendingProviderServiceRequestBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.AcceptedProviderServiceRequestViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.PendingProviderServiceRequestViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AcceptedProviderServiceRequestDialogFragment : MADialogFragment<DialogFragmentAcceptedProviderServiceRequestBinding>() {

    private val viewModel by viewModels<AcceptedProviderServiceRequestViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_accepted_provider_service_request

    override val windowGravity: Int = Gravity.BOTTOM

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForOrder(viewModel.args.orderId),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ORDER
        ) { data ->
            Timber.e("in dialog order details data 0 ->\n$data")

            Handler(Looper.getMainLooper()).post {
                val response = ResponsePusherOrder.fromJsonOrNull(data, gson) ?: return@post

                kotlin.runCatching {
                    if (response.order?.statusOfOrder != ApiOrderStatus.ACCEPTED) {
                        val navController = findNavControllerOfProject()

                        val view = binding?.materialButton ?: return@post

                        navController.executeWhenLoadingEndsIfExists {
                            viewModel.showRequestDetails(view)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}

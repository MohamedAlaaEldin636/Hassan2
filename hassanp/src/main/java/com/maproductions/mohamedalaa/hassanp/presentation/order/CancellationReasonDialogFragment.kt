package com.maproductions.mohamedalaa.hassanp.presentation.order

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentCancellationReasonBinding
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentStopRecievingOrdersBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.StopRecievingOrdersViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.CancellationReasonViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class CancellationReasonDialogFragment : MADialogFragment<DialogFragmentCancellationReasonBinding>() {

    private val viewModel by viewModels<CancellationReasonViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_cancellation_reason

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        val drawable = InsetDrawable(
            AppCompatResources.getDrawable(requireContext(), SR.drawable.dr_top_round_white),
            0,
            requireContext().dpToPx(64f).roundToInt(),
            0,
            0,
        )
        window.setBackgroundDrawable(drawable)
        //window.setBackgroundDrawable(getMyDrawable(SR.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = SR.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.servicesCitiesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesCitiesRecyclerView?.adapter = viewModel.adapter

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleCities) {
            viewModel.adapter.submitList(it.data.orEmpty().map { item ->
                IdAndName(
                    item.id.orZero(),
                    item.text.orEmpty()
                )
            })
        }
    }

}

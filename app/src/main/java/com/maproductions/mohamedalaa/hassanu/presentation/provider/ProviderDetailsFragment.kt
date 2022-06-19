package com.maproductions.mohamedalaa.hassanu.presentation.provider

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentOrderDetailsBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentProviderDetailsBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.OrderDetailsViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.provider.viewModel.ProviderDetailsViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ProviderDetailsFragment : MABaseFragment<FragmentProviderDetailsBinding>() {

    private val viewModel by viewModels<ProviderDetailsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_provider_details

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.previousWorkingsRecyclerView?.layoutManager = object : StaggeredGridLayoutManager(2, VERTICAL) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                val position = kotlin.runCatching { lp?.absoluteAdapterPosition }.getOrNull()

                val context = context

                if (context != null && position != null && lp is LayoutParams) {
                    val actualPosition = if (position < 6) position else position % 6

                    lp.isFullSpan = actualPosition == 0

                    lp.height = (when (actualPosition) {
                        0 -> 156
                        1,3 -> 120
                        2 -> 240
                        else -> 120
                    }).let {
                        context.dpToPx(it.toFloat()).roundToInt()
                    }
                }

                return super.checkLayoutParams(lp)
            }
        }
        binding?.previousWorkingsRecyclerView?.adapter = viewModel.adapterPreviousWorks

        binding?.someClientsOpenionsRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.someClientsOpenionsRecyclerView?.adapter = viewModel.adapterReviews
    }

}

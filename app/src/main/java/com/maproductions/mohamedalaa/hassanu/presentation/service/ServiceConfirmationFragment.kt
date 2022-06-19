package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServiceConfirmationBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServiceConfirmationViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.ResponseCheckPromoCode
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.cos

@AndroidEntryPoint
class ServiceConfirmationFragment : MABaseFragment<FragmentServiceConfirmationBinding>() {

    private val viewModel by viewModels<ServiceConfirmationViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_service_confirmation

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.imagesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding?.imagesRecyclerView?.adapter = viewModel.adapterImages

        binding?.servicesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesRecyclerView?.adapter = viewModel.adapterServicesDetails

        binding?.costRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.costRecyclerView?.adapter = viewModel.adapterServicesSummary

        val imagesUrisAsStrings = viewModel.args.jsonImagesUrisAsStrings.let { json ->
            if (json.isEmpty()) emptyList() else json.fromJson<List<String>>(gson)
        }.mapNotNull {
            kotlin.runCatching { Uri.parse(it) }.getOrNull()
        }
        viewModel.adapterImages.submitList(imagesUrisAsStrings.map { MAImage.IUri(it) })
        viewModel.showImages.value = imagesUrisAsStrings.isNotEmpty()

        viewModel.discount.observe(viewLifecycleOwner) { response ->
            viewModel.adapterServicesSummary.submitList(viewModel.getServicesSummaryList(response))
        }
    }

}

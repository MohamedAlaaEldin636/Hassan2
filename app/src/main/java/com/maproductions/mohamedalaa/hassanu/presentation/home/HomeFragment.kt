package com.maproductions.mohamedalaa.hassanu.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentHomeBinding
import com.maproductions.mohamedalaa.hassanu.presentation.home.viewModel.HomeViewModel
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.bottomNav.viewModel.NavBottomNavViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : MABaseFragment<FragmentHomeBinding>() {

    private val navViewModel by hiltNavGraphViewModels<NavBottomNavViewModel>(R.id.nav_bottom_nav)

    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initializeBindingVariables() {
        binding?.activityViewModel = activityViewModel
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.sliderView?.setSliderAdapter(viewModel.adapterSliders)
        binding?.sliderView?.post {
            binding?.sliderView?.isAutoCycle = true

            binding?.sliderView?.setIndicatorEnabled(true)
            binding?.sliderView?.indicatorSelectedColor = requireContext().getColor(SR.color.indicator_selected_color)
            binding?.sliderView?.indicatorUnselectedColor = requireContext().getColor(SR.color.indicator_unselected_color)

            binding?.sliderView?.startAutoCycle()
        }

        binding?.recyclerView?.layoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapterServices

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            viewModel.viewModelScope.launch {
                viewModel.prefsAccount.updateUserDataLocationData(it.fromJson(gson))
            }
        }

        navViewModel.getHomeResponseHandleRetryAble(this) {
            viewModel.adapterSliders.updateList(it.data?.sliders.orEmpty())
            binding?.sliderView?.setSliderAdapter(viewModel.adapterSliders)

            viewModel.adapterServices.submitList(it.data?.categories.orEmpty())
        }
    }

}

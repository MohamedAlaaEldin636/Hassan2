package com.maproductions.mohamedalaa.shared.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.databinding.FragmentOnBoardBinding
import com.maproductions.mohamedalaa.shared.databinding.FragmentSearchQueriesBinding
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.onBoard.viewModel.OnBoardViewModel
import com.maproductions.mohamedalaa.shared.presentation.search.viewModel.SearchQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchQueriesFragment : MABaseFragment<FragmentSearchQueriesBinding>() {

    companion object {
        const val SAVED_STATE_TEXT = "SearchQueriesFragment.SAVED_STATE_TEXT"
    }

    private val viewModel by viewModels<SearchQueriesViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_search_queries

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
                when (viewModel.args.searchType) {
                    SearchType.USER_HOME -> viewModel.categories.specialCollectLatest {
                        IdAndName(it.id, it.name)
                    }
                    SearchType.USER_ORDERS -> viewModel.suggestionsOfUserOrders.specialCollectLatest {
                        val provider = it.provider?.let { provider ->
                            " - ${provider.name} "
                        } ?: " "

                        IdAndName(
                            it.id,
                            "${it.orderNumber}$provider- ${it.category}"
                        )
                    }
                    SearchType.PROVIDER_HOME -> viewModel.suggestionsOfProviderHomeOrders.specialCollectLatest {
                        IdAndName(
                            it.id,
                            "${it.user?.name.orEmpty()} - ${it.orderNumber}"
                        )
                    }
                }
            }
        }
    }

    private suspend fun <T : Any> Flow<PagingData<T>>.specialCollectLatest(converter: (T) -> IdAndName) {
        collectLatest { pagingData ->
            val newPagingData = pagingData.map { converter(it) to it }

            @Suppress("UNCHECKED_CAST")
            viewModel.adapter.submitData(newPagingData as PagingData<Pair<IdAndName, Any>>)
        }
    }

}

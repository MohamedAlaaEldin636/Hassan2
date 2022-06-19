package com.maproductions.mohamedalaa.shared.core.extensions

import androidx.annotation.StringRes
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.LSAdapterLoadingErrorEmpty

fun PagingDataAdapter<*, *>.withDefaultHeaderAndFooterAdapters(
	@StringRes emptyListMsgStringRes: Int = R.string.no_data_found
): ConcatAdapter {
	val headerAdapter = LSAdapterLoadingErrorEmpty(this, false, emptyListMsgStringRes)
	val footerAdapter = LSAdapterLoadingErrorEmpty(this, true, emptyListMsgStringRes)

	return withCustomAdapters(
		headerAdapter,
		footerAdapter
	)
}

/**
 * - Used since I wanna show loading on launch isa.
 *
 * - No need for prepend to check refresh error as if it happens on launch only append will be
 * notified so no 2 errors above each other will happen isa.
 */
fun PagingDataAdapter<*, *>.withCustomAdapters(
	header: LoadStateAdapter<*>,
	footer: LoadStateAdapter<*>
): ConcatAdapter {
	addLoadStateListener { loadStates ->
		header.loadState = if (loadStates.refresh is LoadState.Loading) {
			LoadState.Loading
		}else {
			loadStates.prepend
		}
		footer.loadState = when (loadStates.refresh) {
			is LoadState.Loading -> {
				LoadState.NotLoading(false)
			}
			is LoadState.Error -> {
				loadStates.refresh
			}
			else -> {
				loadStates.append
			}
		}
	}
	
	return ConcatAdapter(header, this, footer)
}

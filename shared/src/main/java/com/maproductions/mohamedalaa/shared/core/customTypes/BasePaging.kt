package com.maproductions.mohamedalaa.shared.core.customTypes

import androidx.paging.*
import kotlinx.coroutines.flow.Flow

class BasePaging<T : Any>(
	private val fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
) : PagingSource<Int, T>() {

	companion object {
		fun <T : Any> createFlowViaPager(
			fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		): Flow<PagingData<T>> {
			return Pager(
				PagingConfig(10, enablePlaceholders = false)
			) {
				BasePaging {
					fetchPage(it)
				}
			}.flow
		}
	}
	
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val pageNumber = params.key ?: 1 // As 1 is the minimum page number.
		
		return when (val result = fetchPage(pageNumber)) {
			is MAResult.Success -> LoadResult.Page(
				result.value.data?.data.orEmpty(),
				if (pageNumber == 1) null else pageNumber.dec(),
				if (result.value.data?.links?.nextPageUrl == null) null else pageNumber.inc()
			)
			is MAResult.Failure -> LoadResult.Error(MAPagingException(result))
		}
	}
	
	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}
	
}

class BasePagingForChat<T : Any>(
	private val fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
) : PagingSource<Int, T>() {

	companion object {
		fun <T : Any> createFlowViaPager(
			fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		): Flow<PagingData<T>> {
			return Pager(
				PagingConfig(10, enablePlaceholders = false)
			) {
				BasePagingForChat {
					fetchPage(it)
				}
			}.flow
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val pageNumber = params.key ?: 1 // As 1 is the minimum page number.

		return when (val result = fetchPage(pageNumber)) {
			is MAResult.Success -> LoadResult.Page(
				result.value.data?.data.orEmpty().reversed(),
				if (result.value.data?.links?.nextPageUrl == null) null else pageNumber.inc(),
				if (pageNumber == 1) null else pageNumber.dec(),
			)
			is MAResult.Failure -> LoadResult.Error(MAPagingException(result))
		}
	}

	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

}

/*class BasePagingForChat<T : Any>(
	private val fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>,
	private val getLastPage: suspend () -> Int,
) : PagingSource<Int, T>() {
	
	companion object {
		fun <T : Any> createFlowViaPager(
			fetchPage: suspend (Int) -> MAResult.Immediate<MABaseResponse<MABasePaging<T>>>
		): Flow<PagingData<T>> {
			return Pager(
				PagingConfig(10, enablePlaceholders = false)
			) {
				BasePaging {
					fetchPage(it)
				}
			}.flow
		}
	}
	
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		val pageNumber = params.key ?: getLastPage()
		
		return when (val result = fetchPage(pageNumber)) {
			is MAResult.Success -> LoadResult.Page(
				result.value.data?.data.orEmpty(),
				if (pageNumber == 1) null else pageNumber.dec(),
				if (result.value.data?.nextPageUrl == null) null else pageNumber.inc()
			)
			is MAResult.Failure -> LoadResult.Error(MAPagingException(result))
		}
	}
	
	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}
	
}*/

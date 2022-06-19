package com.maproductions.mohamedalaa.shared.presentation.search.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.presentation.search.adapters.viewHolders.VHItemSearchQuery

class RVItemSearchQuery(private val listener: Listener, private val gson: Gson) : PagingDataAdapter<Pair<IdAndName, Any>, VHItemSearchQuery>(COMPARATOR) {

    /** [Map.Entry.key] represents [ServiceInCategory.id] */
    private val map = mutableMapOf<Int, ServiceInCategoryWithCount>()

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Pair<IdAndName, Any>>() {
            override fun areItemsTheSame(
                oldItem: Pair<IdAndName, Any>,
                newItem: Pair<IdAndName, Any>
            ): Boolean = oldItem.first.id == newItem.first.id

            override fun areContentsTheSame(
                oldItem: Pair<IdAndName, Any>,
                newItem: Pair<IdAndName, Any>
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemSearchQuery {
        return VHItemSearchQuery(parent, listener, gson)
    }

    override fun onBindViewHolder(holder: VHItemSearchQuery, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    interface Listener {
        fun onClick(view: View, jsonOfAny: String)
    }

}

package com.maproductions.mohamedalaa.shared.presentation.notification.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.notifications.ItemNotification
import com.maproductions.mohamedalaa.shared.presentation.notification.adapters.viewHolders.VHItemNotification
import com.maproductions.mohamedalaa.shared.presentation.search.adapters.viewHolders.VHItemSearchQuery

class RVItemNotification : PagingDataAdapter<ItemNotification, VHItemNotification>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemNotification>() {
            override fun areItemsTheSame(
                oldItem: ItemNotification,
                newItem: ItemNotification
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ItemNotification,
                newItem: ItemNotification
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemNotification {
        return VHItemNotification(parent)
    }

    override fun onBindViewHolder(holder: VHItemNotification, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

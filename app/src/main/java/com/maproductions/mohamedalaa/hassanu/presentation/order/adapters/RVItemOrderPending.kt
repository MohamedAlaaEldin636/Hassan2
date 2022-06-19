package com.maproductions.mohamedalaa.hassanu.presentation.order.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderPending
import com.maproductions.mohamedalaa.shared.domain.notifications.ItemNotification
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.presentation.notification.adapters.viewHolders.VHItemNotification

class RVItemOrderPending : PagingDataAdapter<ResponseOrder, VHItemOrderPending>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ResponseOrder>() {
            override fun areItemsTheSame(
                oldItem: ResponseOrder,
                newItem: ResponseOrder
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ResponseOrder,
                newItem: ResponseOrder
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemOrderPending {
        return VHItemOrderPending(parent)
    }

    override fun onBindViewHolder(holder: VHItemOrderPending, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

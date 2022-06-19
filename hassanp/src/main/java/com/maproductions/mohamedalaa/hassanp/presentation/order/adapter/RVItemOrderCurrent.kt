package com.maproductions.mohamedalaa.hassanp.presentation.order.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.hassanp.presentation.home.adapters.viewHolder.VHItemOrderInHome
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.viewHolder.VHItemOrderCurrent
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class RVItemOrderCurrent : PagingDataAdapter<ResponseOrder, VHItemOrderCurrent>(COMPARATOR) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemOrderCurrent {
        return VHItemOrderCurrent(parent)
    }

    override fun onBindViewHolder(holder: VHItemOrderCurrent, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

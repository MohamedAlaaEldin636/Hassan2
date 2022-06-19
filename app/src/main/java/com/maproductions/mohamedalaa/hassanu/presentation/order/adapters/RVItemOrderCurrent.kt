package com.maproductions.mohamedalaa.hassanu.presentation.order.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderCurrent
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderPending
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class RVItemOrderCurrent(private val gson: Gson) : PagingDataAdapter<ResponseOrder, VHItemOrderCurrent>(COMPARATOR) {

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
        return VHItemOrderCurrent(parent, gson)
    }

    override fun onBindViewHolder(holder: VHItemOrderCurrent, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

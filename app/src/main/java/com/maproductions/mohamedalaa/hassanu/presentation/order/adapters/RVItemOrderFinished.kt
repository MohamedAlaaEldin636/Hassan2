package com.maproductions.mohamedalaa.hassanu.presentation.order.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderCurrent
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderFinished
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class RVItemOrderFinished(private val gson: Gson) : PagingDataAdapter<ResponseOrder, VHItemOrderFinished>(COMPARATOR) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemOrderFinished {
        return VHItemOrderFinished(parent, gson)
    }

    override fun onBindViewHolder(holder: VHItemOrderFinished, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

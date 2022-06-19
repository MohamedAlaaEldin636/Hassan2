package com.maproductions.mohamedalaa.hassanu.presentation.discount.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.hassanu.presentation.discount.adapter.viewHolder.VHItemCodeOfDiscount
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders.VHItemOrderPending
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class RVItemCodeOfDiscount : PagingDataAdapter<String, VHItemCodeOfDiscount>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemCodeOfDiscount {
        return VHItemCodeOfDiscount(parent)
    }

    override fun onBindViewHolder(holder: VHItemCodeOfDiscount, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

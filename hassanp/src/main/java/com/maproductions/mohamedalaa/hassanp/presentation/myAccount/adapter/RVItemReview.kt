package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder.VHItemReview
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.domain.settings.ItemReview

class RVItemReview : PagingDataAdapter<ItemReview, VHItemReview>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemReview>() {
            override fun areItemsTheSame(
                oldItem: ItemReview,
                newItem: ItemReview
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ItemReview,
                newItem: ItemReview
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemReview {
        return VHItemReview(parent)
    }

    override fun onBindViewHolder(holder: VHItemReview, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

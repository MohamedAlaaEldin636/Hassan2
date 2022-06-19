package com.maproductions.mohamedalaa.shared.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.domain.wallet.ItemWallet
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemWallet

class RVItemWallet : PagingDataAdapter<ItemWallet, VHItemWallet>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemWallet>() {
            override fun areItemsTheSame(
                oldItem: ItemWallet,
                newItem: ItemWallet
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ItemWallet,
                newItem: ItemWallet
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemWallet {
        return VHItemWallet(parent)
    }

    override fun onBindViewHolder(holder: VHItemWallet, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

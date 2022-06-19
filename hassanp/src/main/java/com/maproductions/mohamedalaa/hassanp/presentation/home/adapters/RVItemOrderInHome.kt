package com.maproductions.mohamedalaa.hassanp.presentation.home.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanp.presentation.home.adapters.viewHolder.VHItemOrderInHome
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class RVItemOrderInHome(private val repoOrder: RepoOrder) : PagingDataAdapter<ResponseOrder, VHItemOrderInHome>(COMPARATOR) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemOrderInHome {
        return VHItemOrderInHome(parent, repoOrder) {
            refresh()
        }
    }

    override fun onBindViewHolder(holder: VHItemOrderInHome, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

package com.maproductions.mohamedalaa.shared.presentation.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.presentation.chat.adapter.viewHolder.VHItemChats
import com.maproductions.mohamedalaa.shared.presentation.conversations.ItemConversation

class RVItemChats : PagingDataAdapter<ItemConversation, VHItemChats>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemConversation>() {
            override fun areItemsTheSame(
                oldItem: ItemConversation,
                newItem: ItemConversation
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemConversation,
                newItem: ItemConversation
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemChats {
        return VHItemChats(parent)
    }

    override fun onBindViewHolder(holder: VHItemChats, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}

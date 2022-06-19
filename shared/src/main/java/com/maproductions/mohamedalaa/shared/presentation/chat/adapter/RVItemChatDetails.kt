package com.maproductions.mohamedalaa.shared.presentation.chat.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.maproductions.mohamedalaa.shared.core.customTypes.AccountType
import com.maproductions.mohamedalaa.shared.presentation.chat.adapter.viewHolder.VHItemChatDetails
import com.maproductions.mohamedalaa.shared.presentation.conversations.ItemConversation

class RVItemChatDetails(
    isUserNotProvider: Boolean,
) : PagingDataAdapter<ItemConversation, VHItemChatDetails>(COMPARATOR) {

    private val accountType = if (isUserNotProvider) AccountType.USER else AccountType.PROVIDER

    private var urlOfProfileImage: String = ""

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemConversation>() {
            override fun areItemsTheSame(
                oldItem: ItemConversation,
                newItem: ItemConversation
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemConversation,
                newItem: ItemConversation
            ): Boolean  = oldItem == newItem
        }

        private const val MY_MSG = 1
        private const val OTHER_MSG = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.user?.accountType == accountType) MY_MSG else OTHER_MSG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemChatDetails {
        return VHItemChatDetails(parent, viewType == MY_MSG) {
            urlOfProfileImage
        }
    }

    override fun onBindViewHolder(holder: VHItemChatDetails, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUrlOfProfileImage(url: String) {
        urlOfProfileImage = url

        notifyDataSetChanged()
    }

}

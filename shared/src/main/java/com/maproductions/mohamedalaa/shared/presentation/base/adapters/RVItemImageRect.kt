package com.maproductions.mohamedalaa.shared.presentation.base.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.viewHolders.VHItemImageRect

class RVItemImageRect(private val canDeleteItems: Boolean = true) : ListAdapter<MAImage, VHItemImageRect>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<MAImage>() {
            override fun areItemsTheSame(oldItem: MAImage, newItem: MAImage): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: MAImage, newItem: MAImage): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemImageRect {
        return VHItemImageRect(parent, canDeleteItems) { id ->
            val newList = currentList.toList().toMutableList().also { list ->
                list.removeIf{ it.getId() == id }
            }
            submitList(newList)
        }
    }

    override fun onBindViewHolder(holder: VHItemImageRect, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItemsUri(list: List<Uri>) {
        addItems(list.map { MAImage.IUri(it) })
    }

    fun addItems(list: List<MAImage>) {
        val newList = currentList.toList().toMutableList().also { it.addAll(list) }
        submitList(newList)
    }

}

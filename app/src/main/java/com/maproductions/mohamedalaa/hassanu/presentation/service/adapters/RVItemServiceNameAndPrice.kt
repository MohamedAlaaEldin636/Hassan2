package com.maproductions.mohamedalaa.hassanu.presentation.service.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.viewHolder.VHItemServiceNameAndPrice
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount

class RVItemServiceNameAndPrice(
    private var list: List<ServiceInCategoryWithCount>,
    private val showLastItemAsBold: Boolean
) : RecyclerView.Adapter<VHItemServiceNameAndPrice>() {

    val currentList get() = list

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemServiceNameAndPrice {
        return VHItemServiceNameAndPrice(parent)
    }

    override fun onBindViewHolder(holder: VHItemServiceNameAndPrice, position: Int) {
        val bold = showLastItemAsBold && position == list.lastIndex

        holder.bind(list[position], bold)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ServiceInCategoryWithCount>) {
        this.list = list
        notifyDataSetChanged()
    }

}

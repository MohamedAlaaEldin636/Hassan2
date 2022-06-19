package com.maproductions.mohamedalaa.hassanp.presentation.service.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.service.adapters.viewHolder.VHItemAddingServices
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount

class RVItemAddingServices(private val gson: Gson) : PagingDataAdapter<ServiceInCategory, VHItemAddingServices>(COMPARATOR) {

    /** [Map.Entry.key] represents [ServiceInCategory.id] */
    private val map = mutableMapOf<Int, ServiceInCategoryWithCount>()

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ServiceInCategory>() {
            override fun areItemsTheSame(
                oldItem: ServiceInCategory,
                newItem: ServiceInCategory
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ServiceInCategory,
                newItem: ServiceInCategory
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemAddingServices {
        return VHItemAddingServices(parent, gson) { position, service, increment ->
            incrementOrDecrementThenGetNewCount(position, service, increment)
        }
    }

    override fun onBindViewHolder(holder: VHItemAddingServices, position: Int) {
        val service = getItem(position) ?: return

        holder.bind(position, service, map[service.id]?.count ?: 0)
    }

    fun getSelectedServices(): List<ServiceInCategoryWithCount> = map.values.toList()

    private fun incrementOrDecrementThenGetNewCount(
        position: Int,
        service: ServiceInCategory,
        increment: Boolean
    ) {
        val item = map[service.id]

        if (increment) {
            val newItem = item?.let {
                it.copy(count = it.count.inc())
            } ?: ServiceInCategoryWithCount(service, 1)

            map[service.id] = newItem
        }else if (item != null) {
            if (item.count < 2) {
                map.remove(service.id)
            }else {
                map[service.id] = item.copy(count = item.count.dec())
            }
        }

        notifyItemChanged(position)
    }

}

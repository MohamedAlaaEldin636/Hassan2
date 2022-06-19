package com.maproductions.mohamedalaa.hassanu.presentation.service.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.viewHolder.VHItemAddress
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.viewHolder.VHItemAddressSingleSelection
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import timber.log.Timber

class RVItemAddressSingleSelection : RecyclerView.Adapter<VHItemAddressSingleSelection>() {

    private var list = emptyList<ResponseAddress>()

    private var selectedId = -1

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemAddressSingleSelection {
        return VHItemAddressSingleSelection(parent, this) { id ->
            val prevPosition = this.list.indexOfFirst { it.id == selectedId }

            if (selectedId == id) {
                return@VHItemAddressSingleSelection
            }

            selectedId = id

            val newPosition = this.list.indexOfFirst { it.id == selectedId }

            notifyItemChanged(prevPosition)
            notifyItemChanged(newPosition)
        }
    }

    override fun onBindViewHolder(holder: VHItemAddressSingleSelection, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ResponseAddress>, keepSelection: Boolean = false) {
        this.list = list
        if (this.list.isNotEmpty() && keepSelection.not()) {
            selectedId = this.list.first().id
        }
        notifyDataSetChanged()
    }

    fun getSelectedItemOrNull() = list.firstOrNull { it.id == selectedId }

}
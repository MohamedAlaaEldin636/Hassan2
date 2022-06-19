package com.maproductions.mohamedalaa.shared.presentation.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemTextWithCheckBox
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemTextWithRadio

sealed interface RVItemTextWithRadio {
    fun getSelectedItemOrNull(): IdAndName?
}

class RVItemTextWithRadioFixed : RecyclerView.Adapter<VHItemTextWithRadio>(), RVItemTextWithRadio {

    private var list = emptyList<IdAndName>()

    private var selectedId = -1

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTextWithRadio {
        return VHItemTextWithRadio(parent, this) { id ->
            val prevPosition = this.list.indexOfFirst { it.id == selectedId }

            if (selectedId == id) {
                return@VHItemTextWithRadio
            }

            selectedId = id

            val newPosition = this.list.indexOfFirst { it.id == selectedId }

            notifyItemChanged(prevPosition)
            notifyItemChanged(newPosition)
        }
    }

    override fun onBindViewHolder(holder: VHItemTextWithRadio, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<IdAndName>, keepSelection: Boolean = false) {
        this.list = list
        if (this.list.isNotEmpty() && keepSelection.not()) {
            selectedId = this.list.first().id
        }
        notifyDataSetChanged()
    }

    override fun getSelectedItemOrNull() = list.firstOrNull { it.id == selectedId }

}

class RVItemTextWithRadioPaging(private val listener: Listener? = null) : PagingDataAdapter<IdAndName, VHItemTextWithRadio>(COMPARATOR), RVItemTextWithRadio {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<IdAndName>() {
            override fun areItemsTheSame(
                oldItem: IdAndName,
                newItem: IdAndName
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: IdAndName,
                newItem: IdAndName
            ): Boolean = oldItem == newItem
        }
    }

    private var selectedId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTextWithRadio {
        return VHItemTextWithRadio(parent, this) { id ->
            val prevPosition = snapshot().indexOfFirst { it?.id == selectedId }

            if (selectedId == id) {
                return@VHItemTextWithRadio
            }

            selectedId = id

            val newPosition = snapshot().indexOfFirst { it?.id == selectedId }

            kotlin.runCatching { notifyItemChanged(0) }
            notifyItemChanged(prevPosition)
            notifyItemChanged(newPosition)

            listener?.onChangeSelection(id)
        }
    }

    override fun onBindViewHolder(holder: VHItemTextWithRadio, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun getSelectedItemOrNull() = if (selectedId == -1) snapshot().firstOrNull() else snapshot().firstOrNull { it?.id == selectedId }

    interface Listener {
        fun onChangeSelection(id: Int)
    }

}

class RVItemTextWithCheckBoxPaging : PagingDataAdapter<IdAndName, VHItemTextWithCheckBox>(RVItemTextWithRadioPaging.COMPARATOR) {

    private var selectedIds = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTextWithCheckBox {
        return VHItemTextWithCheckBox(parent, this) { id ->
            val position = snapshot().items.indexOfFirst { it.id == id }

            if (!selectedIds.remove(id)) {
                selectedIds += id
            }

            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: VHItemTextWithCheckBox, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        selectedIds.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItemsIds() = selectedIds.toList()

}

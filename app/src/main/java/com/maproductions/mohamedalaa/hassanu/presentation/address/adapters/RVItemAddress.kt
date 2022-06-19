package com.maproductions.mohamedalaa.hassanu.presentation.address.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.viewHolder.VHItemAddress
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import timber.log.Timber

class RVItemAddress(private val listener: Listener) : RecyclerView.Adapter<VHItemAddress>() {

    private var list = emptyList<ResponseAddress>()

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemAddress {
        return VHItemAddress(parent, listener)
    }

    override fun onBindViewHolder(holder: VHItemAddress, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ResponseAddress>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun deleteAddress(id: Int) {
        val newList = list.toMutableList()
        val index = newList.indexOfFirst { it.id == id }

        Timber.e("abcdef RVItemAddress id $id, index $index\nlist -> $list\nnewList $newList")

        if (index != -1) {
            newList.removeAt(index)

            this.list = newList.toList()
            notifyItemRemoved(index)
        }
    }

    interface Listener {
        /** call api and on success remove manually from list isa. */
        fun deleteAddress(view: View, id: Int)
    }

}

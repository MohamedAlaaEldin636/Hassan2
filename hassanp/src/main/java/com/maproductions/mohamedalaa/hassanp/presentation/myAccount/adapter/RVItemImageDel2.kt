package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder.VHItemImageDel2
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemImageOrVideo
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ItemUri
import kotlin.math.roundToInt

class RVItemImageDel2(
    private val listener: Listener,
    private val gson: Gson,
) : RecyclerView.Adapter<VHItemImageDel2>() {

    private var list: List<ItemUri> = emptyList()

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemImageDel2 {
        return VHItemImageDel2(parent, listener, gson)
    }

    override fun onBindViewHolder(holder: VHItemImageDel2, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ItemUri>) {
        this.list = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<ItemUri>) {
        this.list = this.list + list
        notifyDataSetChanged()
    }

    fun getList(): List<ItemUri> = list.toList()

    interface Listener {
        fun delete(view: View, id: Int, maImageId: String)
    }

}

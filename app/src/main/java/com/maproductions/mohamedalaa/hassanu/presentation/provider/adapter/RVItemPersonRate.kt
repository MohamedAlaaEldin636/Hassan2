package com.maproductions.mohamedalaa.hassanu.presentation.provider.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maproductions.mohamedalaa.hassanu.presentation.provider.adapter.viewHolder.VHItemPersonRate
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.domain.orders.ReviewInProvider
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemImageOrVideo
import kotlin.math.roundToInt

class RVItemPersonRate(
    private var list: List<ReviewInProvider> = emptyList(),
) : RecyclerView.Adapter<VHItemPersonRate>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemPersonRate {
        return VHItemPersonRate(parent)
    }

    override fun onBindViewHolder(holder: VHItemPersonRate, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ReviewInProvider>) {
        this.list = list
        notifyDataSetChanged()
    }

}

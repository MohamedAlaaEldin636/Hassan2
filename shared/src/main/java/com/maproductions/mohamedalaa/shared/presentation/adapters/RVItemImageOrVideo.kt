package com.maproductions.mohamedalaa.shared.presentation.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders.VHItemImageOrVideo
import kotlin.math.roundToInt

class RVItemImageOrVideo(
    private var list: List<ImageOrVideo> = emptyList(),
) : RecyclerView.Adapter<VHItemImageOrVideo>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemImageOrVideo {
        return VHItemImageOrVideo(parent)
    }

    override fun onBindViewHolder(holder: VHItemImageOrVideo, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ImageOrVideo>) {
        this.list = list
        notifyDataSetChanged()
    }

}

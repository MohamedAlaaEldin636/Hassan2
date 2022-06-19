package com.maproductions.mohamedalaa.shared.presentation.onBoard.adapters

import android.view.ViewGroup
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.maproductions.mohamedalaa.shared.presentation.onBoard.adapters.viewHolder.SliderVHOnBoard
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderRVOnBoard : SliderViewAdapter<SliderVHOnBoard>() {

    private var list = emptyList<ItemOnBoard>()

    override fun getCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup): SliderVHOnBoard {
        return SliderVHOnBoard(parent)
    }

    override fun onBindViewHolder(holder: SliderVHOnBoard, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<ItemOnBoard>) {
        this.list = list
        notifyDataSetChanged()
    }

}

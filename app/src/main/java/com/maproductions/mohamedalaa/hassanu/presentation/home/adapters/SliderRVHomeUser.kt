package com.maproductions.mohamedalaa.hassanu.presentation.home.adapters

import android.view.ViewGroup
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.viewHolder.SliderVHHomeUser
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeUser
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderRVHomeUser(private val gson: Gson, private val isGuest: Boolean) : SliderViewAdapter<SliderVHHomeUser>() {

    private var list = emptyList<SliderHomeUser>()

    override fun getCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup): SliderVHHomeUser {
        return SliderVHHomeUser(parent, gson, isGuest)
    }

    override fun onBindViewHolder(holder: SliderVHHomeUser, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<SliderHomeUser>) {
        this.list = list
        notifyDataSetChanged()
    }

}

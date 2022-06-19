package com.maproductions.mohamedalaa.hassanu.presentation.home.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.databinding.ItemServiceInHomeBinding
import com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.viewHolder.VHServiceInHome
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanu.R as AppR
import com.maproductions.mohamedalaa.shared.core.customTypes.SliderLinkType
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.databinding.ItemImageHomeUserSliderBinding
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeUser
import com.smarteist.autoimageslider.SliderViewAdapter

class RVServiceInHome(private val isGuest: Boolean, private val gson: Gson) : ListAdapter<SliderHomeCategory, VHServiceInHome>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<SliderHomeCategory>() {
            override fun areItemsTheSame(
                oldItem: SliderHomeCategory,
                newItem: SliderHomeCategory
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SliderHomeCategory,
                newItem: SliderHomeCategory
            ): Boolean  = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHServiceInHome {
        return VHServiceInHome(parent, isGuest, gson)
    }

    override fun onBindViewHolder(holder: VHServiceInHome, position: Int) {
        holder.bind(getItem(position))
    }

}

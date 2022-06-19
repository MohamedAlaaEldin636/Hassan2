package com.maproductions.mohamedalaa.shared.presentation.search.adapters.viewHolders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.databinding.ItemSearchQueryBinding
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.presentation.search.adapters.RVItemSearchQuery

class VHItemSearchQuery(parent: ViewGroup, private val listener: RVItemSearchQuery.Listener, private val gson: Gson) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_search_query, parent)
) {

    private val binding = ItemSearchQueryBinding.bind(itemView)

    init {
        binding.textView.setOnClickListener {
            val jsonOfAny = binding.textView.tag as? String ?: return@setOnClickListener

            listener.onClick(it, jsonOfAny)
        }
    }

    fun bind(item: Pair<IdAndName, Any>) {
        binding.textView.tag = item.second.toJson(gson)

        binding.textView.text = item.first.name
    }

}

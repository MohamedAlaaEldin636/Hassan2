package com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.ItemServiceInLocationSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVItemAddressSingleSelection
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

class VHItemAddressSingleSelection(
    parent: ViewGroup,
    private val adapter: RVItemAddressSingleSelection,
    private val changeSelection: (Int) -> Unit,
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_service_in_location_selection, parent)
) {

    private val binding = ItemServiceInLocationSelectionBinding.bind(itemView)

    init {
        binding.materialCardView.setOnClickListener {
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            changeSelection(id)
        }
    }

    fun bind(item: ResponseAddress) {
        binding.materialCardView.tag = item.id

        binding.titleTextView.text = item.title

        binding.bodyTextView.text = item.address

        binding.imageView.setImageResource(
            if (adapter.getSelectedItemOrNull()?.id == item.id) {
                SR.drawable.ic_radio_checked
            }else {
                SR.drawable.ic_radio_not_checked
            }
        )
    }

}

package com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setDrawableResEndCompatBA
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.databinding.ItemTextWithCheckboxBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemTextWithRadioBinding
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithCheckBoxPaging
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadio

class VHItemTextWithRadio(
    parent: ViewGroup,
    private val adapter: RVItemTextWithRadio,
    private val changeSelection: (Int) -> Unit,
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_text_with_radio, parent)
) {

    private val binding = ItemTextWithRadioBinding.bind(itemView)

    init {
        binding.textView.setOnClickListener {
            val id = binding.textView.tag as? Int ?: return@setOnClickListener

            changeSelection(id)
        }
    }

    fun bind(item: IdAndName) {
        binding.textView.tag = item.id

        binding.textView.text = item.name

        binding.textView.setDrawableResEndCompatBA(
            if (adapter.getSelectedItemOrNull()?.id == item.id) {
                R.drawable.ic_radio_checked
            }else {
                R.drawable.ic_radio_not_checked
            }
        )
    }

}

class VHItemTextWithCheckBox(
    parent: ViewGroup,
    private val adapter: RVItemTextWithCheckBoxPaging,
    private val toggleSelection: (Int) -> Unit,
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_text_with_checkbox, parent)
) {

    private val binding = ItemTextWithCheckboxBinding.bind(itemView)

    init {
        binding.textView.setOnClickListener {
            val id = binding.textView.tag as? Int ?: return@setOnClickListener

            toggleSelection(id)
        }
    }

    fun bind(item: IdAndName) {
        binding.textView.tag = item.id

        binding.textView.text = item.name

        binding.textView.setDrawableResEndCompatBA(
            if (item.id in adapter.getSelectedItemsIds()) {
                R.drawable.ic_checked_themed
            }else {
                R.drawable.ic_not_checked_5
            }
        )
    }

}

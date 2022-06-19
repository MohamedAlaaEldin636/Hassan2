package com.maproductions.mohamedalaa.hassanu.presentation.discount.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.ItemCodeOfDiscountBinding
import com.maproductions.mohamedalaa.hassanu.databinding.ItemOrderPendingBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class VHItemCodeOfDiscount(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_code_of_discount, parent)
) {

    private val binding = ItemCodeOfDiscountBinding.bind(itemView)

    private val context get() = binding.root.context

    init {
        binding.materialButton.setOnClickListener {
            val code = binding.materialCardView.tag as? String ?: return@setOnClickListener

            it.context.copyToClipboard(code)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: String) {
        binding.materialCardView.tag = item

        binding.textView.text = item
    }

}

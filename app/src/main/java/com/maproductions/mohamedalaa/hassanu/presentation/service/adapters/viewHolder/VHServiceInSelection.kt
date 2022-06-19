package com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.viewHolder

import android.text.style.SubscriptSpan
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.ItemServiceInSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVServiceInSelection
import com.maproductions.mohamedalaa.shared.core.extensions.getTagViaGson
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.setTagViaGson
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory

class VHServiceInSelection(
    parent: ViewGroup,
    private val gson: Gson,
    private val incrementOrDecrementThenGetNewCount: (position: Int, service: ServiceInCategory, increment: Boolean) -> Unit
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_service_in_selection, parent)
) {

    private val binding = ItemServiceInSelectionBinding.bind(itemView)

    init {
        val action = fun (increment: Boolean): String? {
            val position = binding.linearLayout.tag as? Int ?: return null

            return binding.materialCardView.getTagViaGson<ServiceInCategory>(gson)?.run {
                incrementOrDecrementThenGetNewCount(position, this, increment).toString()
            }
        }

        binding.plusImageView.setOnClickListener {
            action(true)
        }

        binding.minusImageView.setOnClickListener {
            action(false)
        }
    }

    fun bind(position: Int, item: ServiceInCategory, count: Int) {
        binding.materialCardView.setTagViaGson(item, gson)
        binding.linearLayout.tag = position

        binding.nameTextView.text = item.name.orEmpty()

        binding.priceTextView.text = buildSpannedString {
            append(item.price.roundHalfUpToIntOrFloat(1).toString())

            val start = length
            append(binding.materialCardView.context.getString(SR.string.currency_symbolic_text))
            val end = length

            this[start..end] = SubscriptSpan()
        }

        binding.numberTextView.text = count.toString()
    }

}
